package com.example.mocker.starter.Routes;

import com.example.mocker.starter.MainApplication;
import com.example.mocker.starter.MainVerticle;
import com.example.mocker.starter.Pojo.RouteInfo;
import com.example.mocker.starter.Service.HealthCheckHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Routes {

  private static final Logger logger = LogManager.getLogger(MainVerticle.class);
  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static Router createRouter(Vertx vertx) {
    Router router = Router.router(vertx);

    HealthCheckHandler healthCheckHandler = new HealthCheckHandler();

    router.get("/healthcheck").handler(healthCheckHandler);

    router.route().handler(BodyHandler.create());

    router.route().handler(routingContext -> {
      String requestUri = routingContext.request().uri();
      String httpMethod = routingContext.request().method().name();
      String body = routingContext.body().asString();
      logger.info("{} {} {}", httpMethod, requestUri, body);
      routingContext.next();
    });

    readMappingsFromFile(vertx, router);

    router.post("/dynamic-routes").handler(routingContext -> {
      String requestBody = routingContext.getBodyAsString();
      JsonObject body = new JsonObject(requestBody);
      String path = body.getString("path");
      String method = body.getString("method");
      JsonObject response = body.getJsonObject("response");

      JsonNode routeDetails;
      try {
        routeDetails = objectMapper.readTree(String.valueOf(response));
      } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
      }
      extracted(router, path, method, routeDetails);
      RouteInfo rf = new RouteInfo(method, path, routeDetails);
      writeRouteToFile(vertx, rf);
      MainApplication.restart(vertx);
      routingContext.response().setStatusCode(201).end();
    });
    return router;
  }

  private static void extracted(Router router, String path, String method, JsonNode routeDetails) {
    Route route = router.route(HttpMethod.valueOf(method), path);
    route.handler(routingContexts -> {
      RouteInfo routeInfo = new RouteInfo(path, method, routeDetails);
      routingContexts.response()
        .putHeader("content-type", "application/json")
        .end(routeInfo.getResponse().toString());
    });
  }

  private static void writeRouteToFile(Vertx vertx, RouteInfo routeInfo) {
    String filename = routeInfo.getPath().replace("/", "_").substring(1).concat("_" + routeInfo.getMethod()) + ".json";
    String userDir = System.getProperty("user.dir");
    Path path = Paths.get(userDir + "/src/main/java/com/example/mocker/starter/Routes/route/" + filename);
    vertx.fileSystem().writeFile(String.valueOf(path), Buffer.buffer(Json.encode(routeInfo)), result -> {
      logger.info(result.succeeded() ? "Route written to file: " + filename : "Failed to write route to file: " + filename);
    });
  }

  private static void readMappingsFromFile(Vertx vertx, Router router) {
    String userDir = System.getProperty("user.dir");
    Path Dirpath = Paths.get(userDir + "/src/main/java/com/example/mocker/starter/Routes/route/");
    vertx.fileSystem().readDir(String.valueOf(Dirpath)).onSuccess(files -> {
      for (String file : files) {
        vertx.fileSystem().readFile(file, result -> {
          if (result.succeeded()) {
            RouteInfo routeInfo = Json.decodeValue(result.result(), RouteInfo.class);
            router.route(HttpMethod.valueOf(routeInfo.getMethod()), routeInfo.getPath()).handler(routingContext -> {
              routingContext.response()
                .putHeader("content-type", "application/json")
                .end(routeInfo.getResponse().toString());
            });
            logger.info("Routes loaded {}", result.result().toString());
          } else {
            System.err.println("Failed to read routes file from disk: " + result.cause().getMessage());
          }
        });
      }
    });
  }
}

package com.example.mocker.starter.Routes;

import com.example.mocker.starter.MainApplication;
import com.example.mocker.starter.MainVerticle;
import com.example.mocker.starter.Pojo.CreateRoute;
import com.example.mocker.starter.Pojo.Validator;
import com.example.mocker.starter.Service.HealthCheckHandler;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class Routes {

  private static final Logger logger = LogManager.getLogger(MainVerticle.class);
  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final String path = "/src/main/java/com/example/mocker/starter/Routes/route/";

  public static Router createRouter(Vertx vertx) {
    Router router = Router.router(vertx);

    HealthCheckHandler healthCheckHandler = new HealthCheckHandler();

    router.get("/healthcheck").handler(healthCheckHandler);

    router.route().handler(BodyHandler.create());

    router.route().failureHandler(Routes::handleException);

    router.route().handler(routingContext -> {
      String requestUri = routingContext.request().uri();
      String httpMethod = routingContext.request().method().name();
      String body = routingContext.body().asString();
      logger.info("{} {} {}", httpMethod, requestUri, body);
      routingContext.next();
    });

    readMappingsFromFile(vertx, router);

//    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

    router.post("/dynamic-routes").handler(routingContext -> {
      JsonObject requestBody = routingContext.getBodyAsJson();
      ObjectMapper objectMapper = new ObjectMapper();
      SimpleModule module = new SimpleModule();
      try {
        module.addDeserializer(CreateRoute.class, new Validator());
        objectMapper.registerModule(module);
      } catch (Exception e) {
        logger.info("Bad Request for route /dynamic-routes {}", e.getMessage());
        routingContext.failure();
      }

      CreateRoute createRoute = null;
      try {
        createRoute = objectMapper.convertValue(requestBody.getMap(), CreateRoute.class);
      } catch (IllegalArgumentException e) {
        logger.info("Bad Request for route /dynamic-routes {}", e.getMessage());
        routingContext.failure();
      }
      addRoute(router, createRoute);
      writeRouteToFile(vertx, createRoute);
      MainApplication.restart(vertx);
      routingContext.response().setStatusCode(201).end();
    });

    return router;
  }

  private static void addRoute(Router router, CreateRoute createRoute) {
    Route route = router.route(HttpMethod.valueOf(createRoute.getRequest().getMethod()), createRoute.getRequest().getPath());
    route.handler(routingContext -> {
      for (Map.Entry<String, String> header : createRoute.getResponse().getHeaders().entrySet()) {
        routingContext.response().putHeader(header.getKey(), header.getValue());
      }
//      routingContext.response().putHeader("content-type", "application/json");
      routingContext.response().setStatusCode(createRoute.getResponse().getStatus()).end(createRoute.getResponse().getBody().toString());
    });
  }

  private static void writeRouteToFile(Vertx vertx, CreateRoute createRoute) {
    String filename = createRoute.getRequest().getPath().replace("/", "_").substring(1).concat("_" + createRoute.getRequest().getMethod()) + ".json";
    String userDir = System.getProperty("user.dir");
    Path filePath = Paths.get(userDir + path + filename);
    vertx.fileSystem().writeFile(String.valueOf(filePath), Buffer.buffer(Json.encode(createRoute)), result -> {
      logger.info(result.succeeded() ? "Route written to file: " + filename : "Failed to write route to file: " + filename);
    });
  }

  private static void readMappingsFromFile(Vertx vertx, Router router) {
    String userDir = System.getProperty("user.dir");
    Path Dirpath = Paths.get(userDir + path);
    vertx.fileSystem().readDir(String.valueOf(Dirpath)).onSuccess(files -> {
      for (String file : files) {
        vertx.fileSystem().readFile(file, result -> {
          if (result.succeeded()) {
            CreateRoute routeInfo = Json.decodeValue(result.result(), CreateRoute.class);
            addRoute(router, routeInfo);
            logger.info("Routes loaded {}", result.result().toString());
          } else {
            System.err.println("Failed to read routes file from disk: " + result.cause().getMessage());
          }
        });
      }
    });
  }

  private static void handleException(RoutingContext routingContext) {
      routingContext.response()
        .setStatusCode(400)
        .end("Bad Request: Invalid JSON body {}");
  }

}


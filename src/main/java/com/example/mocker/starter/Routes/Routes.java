package com.example.mocker.starter.Routes;

import com.example.mocker.starter.Controller.*;
import com.example.mocker.starter.MainVerticle;
import com.example.mocker.starter.Pojo.CreateRoute;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Routes {

  private static final Logger logger = LogManager.getLogger(MainVerticle.class);
  private static final String path = "/src/main/java/com/example/mocker/starter/Routes/route/";
  public static Router router;
  private static Vertx vertx;
  private static void setRouter(Vertx vertx){
    Routes.vertx = vertx;

    router = Router.router(vertx);

    createRoutesFromFiles(vertx, router);

    TransformerMap.loadMap();
  }

  public static Router createRouter(Vertx vertx) {

    setRouter(vertx);

    router.route().handler(BodyHandler.create());

    router.get("/healthcheck").handler(routingContext -> routingContext.response().end("Hello, world!"));

    router.route().failureHandler(Routes::handleException);

    router.route().handler(routingContext -> {
      String requestUri = routingContext.request().uri();
      String httpMethod = routingContext.request().method().name();
      String body = routingContext.body().asString();
      logger.info("{} {} {}", httpMethod, requestUri, body);
      routingContext.next();
    });

    router.post("/dynamic-routes").handler(DynamicRouteHandler::handle);

    router.get("/getkeys").handler(GetKeysController::handle);

    return router;
  }

   public static void addRoute(Router router, CreateRoute createRoute) {
      router.route(HttpMethod.valueOf(createRoute.getRequest().getMethod()), createRoute.getRequest().getPath())
        .handler(new TransformerMockController(vertx,createRoute));
  }

  private static void createRoutesFromFiles(Vertx vertx, Router router) {
    String userDir = Paths.get("").toAbsolutePath().toString();
    Path Dirpath = Paths.get(userDir + path);
    vertx.fileSystem().readDir(Dirpath.toString()).onSuccess(files -> {
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
    String errorMessage = "Invalid Request Body";
    routingContext.response()
        .setStatusCode(400)
        .end(errorMessage);
  }

}


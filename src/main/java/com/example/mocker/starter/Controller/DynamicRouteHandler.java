package com.example.mocker.starter.Controller;


import com.example.mocker.starter.MainApplication;
import com.example.mocker.starter.Pojo.CreateRoute;
import com.example.mocker.starter.Pojo.Validator;
import com.example.mocker.starter.Routes.Routes;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.example.mocker.starter.Routes.Routes.addRoute;

public class DynamicRouteHandler {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  private static final SimpleModule module = new SimpleModule();

  private static final Validator validator =  new Validator();
  private static final String path = "/src/main/java/com/example/mocker/starter/Routes/route/";
  private static final Logger logger = LogManager.getLogger(DynamicRouteHandler.class);

  public static void handle(RoutingContext routingContext) {
    Vertx vertx = routingContext.vertx();
    JsonObject requestBody = routingContext.getBodyAsJson();
    CreateRoute createRoute = validateRequest(routingContext, requestBody);
    addRoute(Routes.router, createRoute);
    writeRouteToFile(vertx, createRoute);
    MainApplication.restart(vertx);
    routingContext.response().putHeader("content-type", "application/json").setStatusCode(201).end(Json.encode(requestBody.getMap()));

  }


  private static CreateRoute validateRequest(RoutingContext routingContext, JsonObject requestBody) {
    module.addDeserializer(CreateRoute.class, validator);
    objectMapper.registerModule(module);
    CreateRoute createRoute = null;
    try {
      createRoute = objectMapper.convertValue(requestBody.getMap(), CreateRoute.class);
    } catch (Exception e) {
      logger.info("Bad Request for route /dynamic-routes {}", e.getMessage());
      routingContext.fail(e);
      routingContext.failure();
    }
    return createRoute;
  }

  private static void writeRouteToFile(Vertx vertx, CreateRoute createRoute) {
    String filename = createRoute.getRequest().getPath().replace("/", "_").substring(1).concat("_" + createRoute.getRequest().getMethod()) + ".json";
    String userDir = System.getProperty("user.dir");
    Path filePath = Paths.get(userDir + path + filename);
    vertx.fileSystem().writeFile(filePath.toString(), Buffer.buffer(Json.encode(createRoute)), result -> {
      logger.info(result.succeeded() ? "Route written to file: " + filename : "Failed to write route to file: " + filename);
    });
  }


}

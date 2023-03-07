package com.example.mocker.starter.Controller;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.List;
import java.util.Map;
public class GetKeysController {
  public static void handle(RoutingContext routingContext) {
    Map<String, List<String>> keys  = Test1.getTransformerKeys();
    JsonObject response = JsonObject.mapFrom(keys);
    routingContext.response().putHeader("content-type", "application/json").setStatusCode(200).end(response.encode());
  }
}

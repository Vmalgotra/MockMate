package com.example.mocker.starter.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class TransformerMockControllers implements Transformer {

  @Override
  public JsonObject transform(RoutingContext routingContext) {
    JsonObject json = routingContext.getBodyAsJson();
    return new JsonObject().put("Hello",json.getValue("test"));
  }
}

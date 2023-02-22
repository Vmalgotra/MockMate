package com.example.mocker.starter.Controller;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class TransformerMockControllers2 implements Transformer {

  @Override
  public JsonObject transform(RoutingContext routingContext) {
    JsonObject json = routingContext.getBodyAsJson();
    return new JsonObject().put("Hanji",json.getValue("test"));
  }
}

package com.example.mocker.starter.Controller.Transformers;

import com.example.mocker.starter.Controller.Transformer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class TransformerMockControllers4 implements Transformer {

  @Override
  public JsonObject transform(RoutingContext routingContext) {
    JsonObject json = routingContext.getBodyAsJson();
    return new JsonObject().put("test",json.getValue("test"));
  }
}

package com.example.mocker.starter.Controller.Transformers;

import com.example.mocker.starter.Controller.Transformer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class TransformerMockControllers implements Transformer {

  @Override
  public JsonObject transform(RoutingContext routingContext) {
    JsonObject json = routingContext.getBodyAsJson();
    return new JsonObject().put("Hello",json.getValue("test"));
  }
}

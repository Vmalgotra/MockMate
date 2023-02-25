package com.example.mocker.starter.Controller.Transformers;

import com.example.mocker.starter.Controller.Transformer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class TransformerMockControllers3 implements Transformer {

  @Override
  public JsonObject transform(RoutingContext routingContext) {
    JsonObject json = routingContext.getBodyAsJson();
    return new JsonObject().put(json.getValue("test").toString(),json.getValue("test"));
  }
}

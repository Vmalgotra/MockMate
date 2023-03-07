package com.example.mocker.starter.Controller.Transformers;

import com.example.mocker.starter.Controller.Transformer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

public class TransformerMockControllers2 implements Transformer {

  @Override
  public JsonObject transform(RoutingContext routingContext) {
    JsonObject json = routingContext.getBodyAsJson();
    return new JsonObject().put("Hanji",json.getValue("test"));
  }
}

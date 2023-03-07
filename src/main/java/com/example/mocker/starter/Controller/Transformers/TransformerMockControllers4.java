package com.example.mocker.starter.Controller.Transformers;

import com.example.mocker.starter.Controller.Transformer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

public class TransformerMockControllers4 implements Transformer {

  public static List<String> transformerKeys = List.of("RoundId","TourId");

  @Override
  public JsonObject transform(RoutingContext routingContext) {
    String id = routingContext.pathParam("id");
    int numericId = Integer.parseInt(id);
    return new JsonObject().put("test",numericId);
  }
}

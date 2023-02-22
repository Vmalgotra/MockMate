package com.example.mocker.starter.Controller;


import com.example.mocker.starter.Pojo.CreateRoute;
import com.fasterxml.jackson.databind.JsonNode;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.Map;

public class TransformerMockController implements Handler<RoutingContext> {

  private CreateRoute createRoute;

  private Vertx vertx;

  public TransformerMockController(Vertx vertx, CreateRoute createRoute) {
    this.vertx = vertx;
    this.createRoute=createRoute;
  }

  @Override
  public void handle(RoutingContext routingContext) {

    Map<String,String> headers = createRoute.getResponse().getHeaders();
    Integer statusCode = createRoute.getResponse().getStatus();
    JsonNode response = createRoute.getResponse().getBody();
    Long delayMs = createRoute.getResponse().getLatency();
    for (Map.Entry<String, String> header : headers.entrySet()) {
        routingContext.response().putHeader(header.getKey(), header.getValue());
      }
    JsonObject finalResponses;
    if(response.isNull()){
      String className = createRoute.getResponse().getTransformer();
      Transformer t = TransformerMap.getObject(className);
      finalResponses = t.transform(routingContext);
    } else {
       finalResponses = JsonObject.mapFrom(response);
    }
    vertx.setTimer(delayMs, timerId -> {
      routingContext.response().setStatusCode(statusCode).end(String.valueOf(finalResponses));
    });
  }

}

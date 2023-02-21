package com.example.mocker.starter.Controller;


import com.example.mocker.starter.Pojo.CreateRoute;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;

import java.util.Map;

public class StaticMockController implements Handler<RoutingContext> {

  private CreateRoute createRoute;

  private Vertx vertx;

  public StaticMockController(Vertx vertx, CreateRoute createRoute) {
    this.vertx = vertx;
    this.createRoute=createRoute;
  }

  @Override
  public void handle(RoutingContext routingContext) {

    Map<String,String> headers = createRoute.getResponse().getHeaders();
    Integer statusCode = createRoute.getResponse().getStatus();
    String response = createRoute.getResponse().getBody().toString();
    Long delayMs = createRoute.getResponse().getLatency();

//    headers.entrySet().stream().map(header -> routingContext.response().putHeader(header.getKey(), header.getValue())).;

    for (Map.Entry<String, String> header : headers.entrySet()) {
        routingContext.response().putHeader(header.getKey(), header.getValue());
      }

    vertx.setTimer(delayMs, timerId -> {
      routingContext.response().setStatusCode(statusCode).end(response);
    });



  }
}

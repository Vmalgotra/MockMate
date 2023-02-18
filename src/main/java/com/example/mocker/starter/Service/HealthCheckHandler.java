package com.example.mocker.starter.Service;


import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public class HealthCheckHandler implements Handler<RoutingContext> {

  @Override
  public void handle(RoutingContext routingContext) {
    routingContext.response().end("Hello, world!");
  }
}

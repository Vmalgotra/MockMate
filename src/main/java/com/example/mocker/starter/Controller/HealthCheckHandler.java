package com.example.mocker.starter.Controller;

import io.vertx.ext.web.RoutingContext;

public class HealthCheckHandler{

  public static void handle(RoutingContext routingContext) {
    routingContext.response().end("Hello, world!");
  }
}

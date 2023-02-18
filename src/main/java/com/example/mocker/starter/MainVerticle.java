package com.example.mocker.starter;

import com.example.mocker.starter.HttpServer.MyHttpServer;
import com.example.mocker.starter.Routes.Routes;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class MainVerticle extends AbstractVerticle {

  private static final Logger logger = LogManager.getLogger(MainVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    Router router = Routes.createRouter(vertx);
    MyHttpServer.startHttpServer(vertx, router);

    startPromise.complete();
  }
}

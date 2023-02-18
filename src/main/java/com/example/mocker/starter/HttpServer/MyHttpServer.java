package com.example.mocker.starter.HttpServer;

import com.example.mocker.starter.MainApplication;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MyHttpServer {

  private static final Logger logger = LogManager.getLogger(MyHttpServer.class);


  public static void startHttpServer(Vertx vertx, Router router) {
    HttpServer server = vertx.createHttpServer(new HttpServerOptions().setPort(8888));
    server.requestHandler(router).listen(ar -> {
      if (ar.succeeded()) {
        logger.info("HTTP server started on port 8888");
      } else {
        logger.info("Failed to start HTTP server");
        ar.cause().printStackTrace();
      }
    });
  }
}

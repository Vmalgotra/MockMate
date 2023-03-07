package com.example.mocker.starter;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainApplication {

  private static final Logger logger = LogManager.getLogger(MainApplication.class);
  public static void main(String[] args) {
    deployVerticles();
  }
  public static void restart (Vertx vertxObject) {
    vertxObject.close(event -> {
      if (event.succeeded()) {
        System.out.println("Vert.x stopped successfully");
        deployVerticles();
      } else {
        System.out.println("Vert.x stop failed");
      }
    });
  }
  private static void deployVerticles() {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(MainVerticle.class.getName(), new DeploymentOptions().setInstances(4), ar -> {
      if (ar.succeeded()) {
        logger.info("Main verticle deployed successfully");
      } else {
        logger.info("Failed to deploy main verticle");
        ar.cause().printStackTrace();
      }
    });
  }

}

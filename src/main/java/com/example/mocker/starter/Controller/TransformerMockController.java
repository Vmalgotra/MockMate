package com.example.mocker.starter.Controller;


import com.example.mocker.starter.Pojo.CreateRoute;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.List;
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
    String expression = createRoute.getResponse().getExpression();

    for (Map.Entry<String, String> header : headers.entrySet()) {
        routingContext.response().putHeader(header.getKey(), header.getValue());
      }
    JsonObject finalResponses;
    if(response.isNull()){
      String className = createRoute.getResponse().getTransformer();
      Transformer t = TransformerMap.getObject(className);
      finalResponses = t.transform(routingContext);
    } else if (expression != null) {
      String id = routingContext.pathParam("id");
      JsonPointer pointer = JsonPointer.compile(expression);
      JsonNode parentNode = response.at(pointer.head());
      JsonNode trNode = response.at(pointer);
//      List<JsonNode> nodesToUpdate = response.at(pointer).findValues(pointer.last().getMatchingProperty());

      // Create a new JsonNode with the updated value
      JsonNode newValue = null;
      if (trNode != null && trNode.isValueNode()) {
        if (trNode.isInt()) {
          newValue = JsonNodeFactory.instance.numberNode(Byte.parseByte(id));
        } else if (trNode.isTextual()) {
          newValue = JsonNodeFactory.instance.textNode(id);
        } else if (trNode.isBoolean()){
          newValue = JsonNodeFactory.instance.booleanNode(Boolean.parseBoolean(id));
        }
      }
      if (newValue != null) {
        ((ObjectNode) parentNode).set(pointer.last().getMatchingProperty(), newValue);
      }
      finalResponses = JsonObject.mapFrom(response);

    } else {
       finalResponses = JsonObject.mapFrom(response);
    }
    vertx.setTimer(delayMs, timerId -> {
      routingContext.response().setStatusCode(statusCode).end(String.valueOf(finalResponses));
    });
  }

}

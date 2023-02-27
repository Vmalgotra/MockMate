package com.example.mocker.starter.Controller;

import com.example.mocker.starter.Pojo.CreateRoute;
import com.example.mocker.starter.Pojo.Response;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class TransformerMockController implements Handler<RoutingContext> {

  private CreateRoute createRoute;
  private Vertx vertx;

  public TransformerMockController(Vertx vertx, CreateRoute createRoute) {
    this.vertx = vertx;
    this.createRoute=createRoute;
  }

  @Override
  public void handle(RoutingContext routingContext) {

    Response createRouteResponse = createRoute.getResponse();
    JsonNode response = createRouteResponse.getBody();
    String expression = createRouteResponse.getExpression();
    createRouteResponse.getHeaders().forEach((key, value) -> routingContext.response().putHeader(key, value));

    JsonObject finalResponses;
    if (response.isNull()) {
      String className = createRouteResponse.getTransformer();
      Transformer t = TransformerMap.getObject(className);
      finalResponses = t.transform(routingContext);
    } else if (expression != null) {
      String id = routingContext.pathParams().values().iterator().next();
      JsonPointer pointer = JsonPointer.compile(expression);
      JsonNode parentNode = response.at(pointer.head());
      JsonNode trNode = response.at(pointer);

      JsonNode newValue = null;
      if (trNode != null && trNode.isValueNode()) {
        if (trNode.isInt()) {
          newValue = JsonNodeFactory.instance.numberNode(Byte.parseByte(id));
        } else if (trNode.isTextual()) {
          newValue = JsonNodeFactory.instance.textNode(id);
        } else if (trNode.isBoolean()) {
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
    vertx.setTimer(createRouteResponse.getLatency(), timerId -> {
      routingContext.response().setStatusCode(createRouteResponse.getStatus()).end(finalResponses.encode());
    });
  }

}

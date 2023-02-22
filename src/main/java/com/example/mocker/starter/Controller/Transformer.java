package com.example.mocker.starter.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public interface Transformer {
  /**
   * Transforms the body of the provided RoutingContext into a JSON object and
   * returns a new JSON object
   *
   * @param routingContext the RoutingContext object containing the body to be transformed
   * @return a JSON string representing the transformed object
   */
  JsonObject transform(RoutingContext routingContext);
}

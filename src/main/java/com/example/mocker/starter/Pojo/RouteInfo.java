package com.example.mocker.starter.Pojo;

import com.fasterxml.jackson.databind.JsonNode;
import io.vertx.core.json.JsonObject;

public class RouteInfo {
  private String method;
  private String path;
  private JsonNode response;

  public RouteInfo() {
    // Default constructor required for Jackson
  }

  public RouteInfo(String method, String path, JsonNode response) {
    this.method = method;
    this.path = path;
    this.response = response;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public JsonNode getResponse() {
    return response;
  }

  public void setResponse(JsonNode response) {
    this.response = response;
  }
}

package com.example.mocker.starter.Pojo;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public class Response {
  private JsonNode body;

  public Map<String, String> getHeaders() {
    return headers;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }


  public void setHeaders(Map headers) {
    this.headers = headers;
  }

  private Integer status;

  private Map<String,String> headers;

  public JsonNode getBody() {
    return body;
  }

  public void setBody(JsonNode body) {
    this.body = body;
  }
}

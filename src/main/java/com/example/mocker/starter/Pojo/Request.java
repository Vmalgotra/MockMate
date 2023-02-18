package com.example.mocker.starter.Pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Request {

  @JsonProperty(required = true)
  private String method;

  @JsonProperty(required = true)
  private String path;

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
}

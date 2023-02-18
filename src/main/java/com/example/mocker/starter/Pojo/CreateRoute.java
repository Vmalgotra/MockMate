package com.example.mocker.starter.Pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateRoute {

  @JsonProperty(required = true)
  private Request request;

  @JsonProperty(required = true)
  private Response response;

  public Request getRequest() {
    return request;
  }

  public void setRequest(Request request) {
    this.request = request;
  }

  public Response getResponse() {
    return response;
  }

  public void setResponse(Response response) {
    this.response = response;
  }
}


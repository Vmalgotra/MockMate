package com.example.mocker.starter.Pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;


@Data
@NoArgsConstructor
public class CreateRoute {
  @NonNull
  @JsonProperty(required = true)
  private Request request;
//  @NonNull
  @JsonProperty(required = true)
  private Response response;
}


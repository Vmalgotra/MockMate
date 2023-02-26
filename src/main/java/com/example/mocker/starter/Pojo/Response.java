package com.example.mocker.starter.Pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import java.util.Map;

@Data
@NoArgsConstructor
public class Response {

  private JsonNode body = null;
//  @JsonProperty(required = true)
  private Integer status = 200;
//  @JsonProperty(required = true)
  @NonNull
  private Map<String,String> headers;

  private Long latency=0L;

  private String transformer;

  private String expression = null;

}

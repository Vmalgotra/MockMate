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

  @JsonProperty(required = true)
  @NonNull
  private JsonNode body;
//  @JsonProperty(required = true)
  @NonNull
  private Integer status;
//  @JsonProperty(required = true)
  @NonNull
  private Map<String,String> headers;

  private Long latency=0L;

}

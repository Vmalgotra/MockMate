package com.example.mocker.starter.Pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

//import javax.annotation.Nonnull;

@Data
@NoArgsConstructor
public class Request {

//  @JsonProperty(required = true)
  @NonNull
  private String method;
//  @JsonProperty(required = true)
 @NonNull
  private String path;
}

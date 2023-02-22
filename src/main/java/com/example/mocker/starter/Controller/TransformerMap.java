package com.example.mocker.starter.Controller;

import java.util.HashMap;
import java.util.Map;

public class TransformerMap {

  private static Map<String, Transformer> classMap = new HashMap<>();

  public static void loadMap() {
    classMap.put("TransformerMockControllers", new TransformerMockControllers());
    classMap.put("TransformerMockControllers2", new TransformerMockControllers2());
  }

  public static Transformer getObject(String transformerName){
    return classMap.get(transformerName);
  }
}

package com.example.mocker.starter.Controller;

import com.example.mocker.starter.Controller.Transformers.TransformerMockControllers;
import com.example.mocker.starter.Controller.Transformers.TransformerMockControllers4;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test1{
  private static Map<String,List<String>> transformerKeys = new HashMap<>();

  static {
    transformerKeys.put("TransformerMockControllers",List.of("RoundId","TourId"));
    transformerKeys.put("TransformerMockControllers1",List.of("RoundId1","TourId1"));
    transformerKeys.put("TransformerMockControllers2",List.of("RoundId2","TourId2"));
    transformerKeys.put("TransformerMockControllers3",List.of("RoundId3","TourId3"));
  }

  static Map<String,List<String>> getTransformerKeys(){
//    new TransformerMockControllers4().

    return transformerKeys;
  }


}

//package com.example.mocker.starter.Controller;
//
//import com.example.mocker.starter.Controller.Transformers.TransformerMockControllers4;
//
//import java.lang.reflect.Field;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class LoadTransformerKeys {
//
//  public static void main(String[] args) {
//    loadFieldTransformers();
//  }
//  public static Map<String, List<String>> loadFieldTransformers(Class<?> clazz) {
//    Map<String, List<String>> transformerMap = new HashMap<>();
//
//    List<String> transformerKeys1 = TransformerMockControllers4.transformerKeys;
//
//    Field[] fields = clazz.getDeclaredFields();
//    for (Field field : fields) {
//      List<String> transformerKeys = field.getAnnotation(FieldNameTransformer.class);
//      if (transformerKeys != null) {
//        transformerMap.put(clazz.getName(), transformerKeys);
//      }
//    }
//
//    return transformerMap;
//  }
//}

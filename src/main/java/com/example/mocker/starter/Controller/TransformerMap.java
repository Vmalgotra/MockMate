package com.example.mocker.starter.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TransformerMap {

  private static Map<String, Transformer> classMap;

  private static final Logger logger = LogManager.getLogger(TransformerMap.class);

  @SneakyThrows
  public static void loadMap() {
    classMap = loadClassesFromPackage("com.example.mocker.starter.Controller.Transformers");
  }

  public static Transformer getObject(String transformerName){
    return classMap.get(transformerName);
  }

  public static HashMap<String, Transformer> loadClassesFromPackage(String packageName) {
    HashMap<String, Transformer> classMap = new HashMap<>();

    try (ScanResult scanResult = new ClassGraph().whitelistPackages(packageName).enableClassInfo().scan()) {
      List<Class<?>> classNames = scanResult.getAllClasses().loadClasses();

      for (Class<?> clazz : classNames) {
        Transformer transformer = (Transformer) clazz.newInstance();
        String[] classNames1 = clazz.getName().split("\\.");
        classMap.put(classNames1[classNames1.length - 1], transformer);
      }
    } catch (InstantiationException | IllegalAccessException e) {
      logger.info(e.getMessage());
    }

    return classMap;
  }
}

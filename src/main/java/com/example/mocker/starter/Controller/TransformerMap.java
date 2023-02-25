package com.example.mocker.starter.Controller;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.mocker.starter.MainVerticle;
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

  public static HashMap<String, Transformer> loadClassesFromPackage(String packageName) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    HashMap<String, Transformer> classMap = new HashMap<>();

    List<Class<?>> classNames;

    try(ScanResult scanResult = new ClassGraph().whitelistPackages(packageName).enableClassInfo().scan()) {
      classNames = scanResult.getAllClasses().loadClasses();

      for (int i=0; i<classNames.size();i++) {
        Object object = classNames.get(i).newInstance();
        String[] classNames1 = classNames.get(i).getName().split("\\.");
        Integer classCount = classNames1.length;
        classMap.put(classNames1[classCount - 1], (Transformer) object);
      }
    } catch (Exception e) {
      logger.info(e.getMessage());
    }

    return classMap;
  }
}

package com.example.mocker.starter.Controller;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import com.example.mocker.starter.Controller.Transformers.*;
import lombok.SneakyThrows;

public class TransformerMap {

  private static Map<String, Transformer> classMap;

  @SneakyThrows
  public static void loadMap() {
    classMap = loadClassesFromPackage("com.example.mocker.starter.Controller.Transformers");
  }

  public static Transformer getObject(String transformerName){
    return classMap.get(transformerName);
  }

  public static HashMap<String, Transformer> loadClassesFromPackage(String packageName) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    HashMap<String, Transformer> classMap = new HashMap<>();

    // Get the ClassLoader for the current thread
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    // Get the package directory as a resource
    String packageDirectory = packageName.replace('.', '/');
    URL packageUrl = classLoader.getResource(packageDirectory);
    String a = packageUrl.getFile();
    // Get a list of all files in the package directory
    File[] packageFiles = new File(packageUrl.getFile()).listFiles();

    // Iterate over the files and load any .class files
    for (File packageFile : packageFiles) {
      String fileName = packageFile.getName();
      if (fileName.endsWith(".class")) {
        // Remove the .class file extension
        String className = packageName + '.' + fileName.substring(0, fileName.length() - 6);

        // Load the class and instantiate an object
        Class<?> clazz = classLoader.loadClass(className);
        Object object = clazz.newInstance();

        String[] classNames = clazz.getName().split("\\.");
        Integer classCount = classNames.length;

        // Add the object to the map using the class name as the key
        classMap.put(classNames[classCount-1], (Transformer) object);
      }
    }

    return classMap;
  }
}

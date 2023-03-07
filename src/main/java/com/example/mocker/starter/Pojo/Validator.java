package com.example.mocker.starter.Pojo;

import com.example.mocker.starter.MainVerticle;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Map;

public class Validator extends StdDeserializer<CreateRoute> {

  private static final Logger logger = LogManager.getLogger(Validator.class);

  public Validator() {
    super(CreateRoute.class);
  }

  @Override
  public CreateRoute deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
    ObjectCodec codec = jsonParser.getCodec();
    JsonNode node = codec.readTree(jsonParser);

    JsonNode requestNode = node.get("request");
    JsonNode responseNode = node.get("response");
    JsonNode status = responseNode.get("status");
    JsonNode body = responseNode.get("body");
    JsonNode headers =  responseNode.get("headers");
    JsonNode path =  requestNode.get("path");
    JsonNode method =  requestNode.get("method");
    JsonNode transformer =  responseNode.get("transformer");


    if (requestNode == null || requestNode.isNull()) {
      logger.info("Missing 'request' field");
      throw new JsonParseException(jsonParser, "Missing 'request' field");
    }
    if (responseNode == null || responseNode.isNull()) {
      logger.info("Missing 'response' field");
      throw new JsonParseException(jsonParser, "Missing 'response' field");
    }
//    if (status == null ) {
//      logger.info("Missing 'status' field");
//      throw new JsonParseException(jsonParser, "Missing 'status' field");
//    }

    if (headers == null || headers.isNull()) {
      logger.info("Missing 'headers' field");
      throw new JsonParseException(jsonParser, "Missing 'headers' field");
    }

    if (body == null && transformer == null) {
      logger.info("Missing 'body' or 'transformer' field");
      throw new JsonParseException(jsonParser, "Missing 'body' or 'transformer' field");
    }

    if (path == null || path.isNull()) {
      logger.info("Missing 'path' field");
      throw new JsonParseException(jsonParser, "Missing 'path' field");
    }

    if (method == null || method.isNull()) {
      logger.info("Missing 'method' field");
      throw new JsonParseException(jsonParser, "Missing 'method' field");
    }

    Request request = codec.treeToValue(requestNode, Request.class);
    Response response = codec.treeToValue(responseNode, Response.class);

    CreateRoute createRoute = new CreateRoute();
    createRoute.setRequest(request);
    createRoute.setResponse(response);
    return createRoute;
  }
}


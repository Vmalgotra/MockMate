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
    String path = String.valueOf(responseNode.get("path"));
    JsonNode body = responseNode.get("body");
    Map headers = (Map) node.get("headers");
    if (requestNode == null || responseNode == null || requestNode.isNull() || responseNode.isNull()) {
      logger.info("Missing 'request' field");
      throw new JsonParseException(jsonParser, "Missing 'request' field");
    }
    if (responseNode == null || responseNode.isNull()) {
      logger.info("Missing 'response' field");
      throw new JsonParseException(jsonParser, "Missing 'response' field");
    }
    if (path == null || path == "") {
      logger.info("Missing 'path' field");
      throw new JsonParseException(jsonParser, "Missing 'path' field");
    }

    if (body == null || body.isNull()) {
      logger.info("Missing 'body' field");
      throw new JsonParseException(jsonParser, "Missing 'body' field");
    }
    Request request = codec.treeToValue(requestNode, Request.class);
    Response response = codec.treeToValue(responseNode, Response.class);

    CreateRoute createRoute = new CreateRoute();
    createRoute.setRequest(request);
    createRoute.setResponse(response);
    return createRoute;
  }
}


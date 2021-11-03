package com.baloise.open.edw.domain.kafka;

import lombok.Data;

import java.io.Serializable;

@Data
public class Status implements Serializable {

  public enum EventType {
    TOPIC_CREATED,
    CONNECT,
    SHUTDOWN
  }

  private final String clientId;
  private final String topic;
  private final EventType eventType;


  public String toJson() {
    return String.format("{ " +
        "\"clientId\" : \"%s\", " +
        "\"topic\" : \"%s\", " +
        "\"eventType\" : \"%s\" " +
        "}", clientId, topic, eventType);
  }
}

package com.baloise.open.edw.infrastructure.kafka;

import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public interface Producer {

  static Producer create(Properties configProps, String topic, String clientId) throws ExecutionException, InterruptedException {
    return new ProducerImpl(configProps, topic, clientId);
  }

  /**
   * Pushes an event to the given topic
   */
  Future<RecordMetadata> pushEvent(String topic, String key, Object value);

  /**
   * Creates a status event in event topic {@link Config#STATUS_TOPIC_NAME} when producer connects
   */
  void pushStatusProducerConnected();

  /**
   * Creates a status event in event topic {@link Config#STATUS_TOPIC_NAME} when producer is shutting down.
   * To be called in @PostDestroy method of Producer
   */
  void pushStatusProducerShutdown();

  /**
   * Creates a status event in event topic {@link Config#STATUS_TOPIC_NAME} when producer creates new Topic
   */
  void pushStatusTopicCreated();
}

package com.baloise.open.edw.infrastructure.kafka;

public interface Workflow {

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

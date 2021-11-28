package com.baloise.open.edw.infrastructure.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Properties;

public interface Consumer extends Workflow, Runnable {

  static Consumer create (Properties configProps, String topic, String clientId, java.util.function.Consumer<? super ConsumerRecord<String, Object>> consumer) {
    return new ConsumerImpl(configProps, topic, clientId, consumer);
  }

  void shutdown();
}

package com.baloise.open.edw.infrastructure.kafka;

import lombok.Builder;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class ConsumerImpl extends Config implements Consumer {

  private final KafkaConsumer<String, String> kafkaConsumer;
  private final AtomicBoolean isShutdown = new AtomicBoolean(false);

  /**
   * Function processing consumed records
   */
  @Setter
  private java.util.function.Consumer<? super ConsumerRecord<String, String>> recordConsumer;

  /**
   * Topic poll interval in milliseconds
   */
  @Setter
  private long pollTime = 1000;

  @Builder
  protected ConsumerImpl(Properties configProps, String topic, String clientId, java.util.function.Consumer<? super ConsumerRecord<String, String>> consumer) {
    super(configProps, topic, clientId);
    this.kafkaConsumer = new KafkaConsumer<>(getConfigProps());
    this.recordConsumer = consumer;
  }

  @Override
  public void run() {
    log.info("run consumer");
    try {
      kafkaConsumer.subscribe(Collections.singleton(getTopic()));
      while (!isShutdown.get()) {
        kafkaConsumer.poll(Duration.of(pollTime, ChronoUnit.MILLIS)).forEach(recordConsumer);
      }
    } finally {
      log.info("Consumer shutdown ");
      kafkaConsumer.close();
    }
  }

  @Override
  public void pushStatusProducerConnected() {
    throw new RuntimeException("Not yet implemented");

  }

  @Override
  public void pushStatusProducerShutdown() {
    throw new RuntimeException("Not yet implemented");
  }

  @Override
  public void pushStatusTopicCreated() {
    throw new RuntimeException("Not yet implemented");
  }
}

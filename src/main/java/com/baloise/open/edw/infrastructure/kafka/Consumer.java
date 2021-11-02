package com.baloise.open.edw.infrastructure.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Consumer extends Config implements Runnable {

  private final KafkaConsumer<String, String> kafkaConsumer;
  private final AtomicBoolean isShutdown = new AtomicBoolean(false);

  protected Consumer(Properties configProps, String topic, String clientId) {
    super(configProps, topic, clientId);
    this.kafkaConsumer = new KafkaConsumer<>(getConfigProps());
  }

  public abstract void process(ConsumerRecord<String, String> consumerRecord);

  @Override
  //TODO: parameterize poll interval
  public void run() {
    LOGGER.info("run consumer");
    try {
      kafkaConsumer.subscribe(Collections.singleton(getTopic()));
      while (!isShutdown.get()) {
        kafkaConsumer.poll(Duration.of(1000, ChronoUnit.MILLIS)).forEach(this::process);
      }
    } finally {
      LOGGER.info("Consumer shutdown ");
      kafkaConsumer.close();
    }
  }
}

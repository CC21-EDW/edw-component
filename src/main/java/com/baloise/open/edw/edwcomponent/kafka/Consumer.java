package com.baloise.open.edw.edwcomponent.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Consumer extends Config implements Runnable {

  private static final Logger logger = LoggerFactory.getLogger(Consumer.class);
  private final KafkaConsumer<String, String> consumer;
  private final AtomicBoolean isShutdown = new AtomicBoolean(false);


  public Consumer(Properties configProps, String topic, String clientId) {
    super(configProps, topic, clientId);
    this.consumer = new KafkaConsumer<>(getConfigProps());
  }

  public abstract void process(ConsumerRecord<String, String> record);

  //TODO: parameterize poll interval
  public void run() {
    try {
      consumer.subscribe(Collections.singleton(getTopic()));
      while (!isShutdown.get()) {
        consumer.poll(Duration.of(1000, ChronoUnit.MILLIS)).forEach(this::process);
      }
    } finally {
      logger.info("Consumer shutdown ");
      consumer.close();
    }
  }
}

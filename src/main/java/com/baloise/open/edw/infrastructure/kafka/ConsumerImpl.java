package com.baloise.open.edw.infrastructure.kafka;

import com.baloise.open.edw.domain.kafka.Status;
import com.baloise.open.edw.domain.services.CorrelationId;
import lombok.Builder;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

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

  /**
   * Creates a status event in event topic {@link Config#STATUS_TOPIC_NAME} when producer connects
   */
  private void pushStatusProducerConnected() {
    final Status status = new Status(getClientId(), getTopic(), Status.EventType.CONNECT);
    pushStatusEvent(status);
    log.info("Connected producer with ID '{}' to workflow.", getClientId());
  }

  @Override
  public void pushStatusProducerShutdown() {
    final Status status = new Status(getClientId(), getTopic(), Status.EventType.SHUTDOWN);
    pushStatusEvent(status);
    log.info("Disconnected producer with ID '{}' from workflow.", getClientId());
  }

  @Override
  public void pushStatusTopicCreated() {
    final Status status = new Status(getClientId(), getTopic(), Status.EventType.TOPIC_CREATED);
    pushStatusEvent(status);
    log.info("Created topic with name '{}' by producer with ID '{}' from workflow.", getTopic(), getClientId());
  }

  private void pushStatusEvent(Status status) {
    final String correlationId = generateDefaultCorrelationId();
    CorrelationId.set(correlationId);

    try (final KafkaProducer<String, Object> producer = new KafkaProducer<>(getConfigProps())) {
      producer.send(new ProducerRecord<>(STATUS_TOPIC_NAME, correlationId, status.toJson()));
    }
  }
}

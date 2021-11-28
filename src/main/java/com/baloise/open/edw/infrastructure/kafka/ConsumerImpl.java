package com.baloise.open.edw.infrastructure.kafka;

import com.baloise.open.edw.domain.kafka.Status;
import com.baloise.open.edw.domain.services.CorrelationId;
import com.baloise.open.edw.infrastructure.kafka.mapper.StatusDtoMapper;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class ConsumerImpl extends AbstractWorkflow implements Consumer {

  private final KafkaConsumer<String, Object> kafkaConsumer;
  private final AtomicBoolean isShutdown = new AtomicBoolean(false);

  ExecutorService executorService = Executors.newSingleThreadExecutor();

  /**
   * Function processing consumed records
   */
  @Setter
  private java.util.function.Consumer<? super ConsumerRecord<String, Object>> recordConsumer;

  /**
   * Topic poll interval in milliseconds
   */
  @Setter
  private long pollTime = 1000;

  @Builder
  protected ConsumerImpl(Properties configProps, String topic, String clientId, java.util.function.Consumer<? super ConsumerRecord<String, Object>> consumer) {
    super(configProps, topic, clientId);
    this.kafkaConsumer = new KafkaConsumer<>(getConfigProps());
    this.recordConsumer = consumer;
  }

  @Override
  public void run() {
    executorService.execute(new ConsumerExecutor());
  }

  void pushStatusEvent(Status status) {
    final String correlationId = generateDefaultCorrelationId();
    CorrelationId.set(correlationId);

    try (final KafkaProducer<String, Object> producer = new KafkaProducer<>(getConfigProps())) {
      producer.send(new ProducerRecord<>(STATUS_TOPIC_NAME, correlationId, StatusDtoMapper.INSTANCE.map(status)));
    }
  }

  @Override
  public void shutdown() {
    this.isShutdown.set(true);
    executorService.shutdown();
  }

  final class ConsumerExecutor implements Runnable {

    @Override
    public void run() {
      log.info("run consumer");
      try {
        kafkaConsumer.subscribe(Collections.singleton(getTopic()));
        pushStatusConnected();
        while (!isShutdown.get()) {
          kafkaConsumer.poll(Duration.of(pollTime, ChronoUnit.MILLIS)).forEach(recordConsumer);
        }
      } finally {
        log.info("Consumer shutdown ");
        pushStatusShutdown();
        kafkaConsumer.close();
      }
    }
  }
}

package com.baloise.open.edw.infrastructure.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Properties;

@Slf4j
class ConsumerTest extends Consumer {

  public ConsumerTest(Properties configProps, String topic, String clientId) {
    super(configProps, topic, clientId);
  }

  public static void main(String[] args) {
    new ConsumerTest(new Properties(), "CC21_EDW_2128", "myTestClient").run();
  }

  @Override
  public void process(ConsumerRecord<String, String> record) {
    log.info(String.format("consumed message %s:%s", record.key(), record.value()));
  }
}

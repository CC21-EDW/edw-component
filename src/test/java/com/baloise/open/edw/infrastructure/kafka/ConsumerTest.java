package com.baloise.open.edw.infrastructure.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Properties;

class ConsumerTest extends Consumer {

  public ConsumerTest(Properties configProps, String topic, String clientId) {
    super(configProps, topic, clientId);
  }

  public static void main(String[] args) {
    new ConsumerTest(new Properties(), "CC21_EDW_2128", "myTestClient").run();
  }

  @Override
  public void process(ConsumerRecord<String, String> record) {
    System.out.printf("consumed message %s:%s", record.key(), record.value());
  }
}

package com.baloise.open.edw.infrastructure.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class ConsumerTest extends BaseKafkaTest {

  @Test
  void verifyInit() {
    final ConsumerImpl testee = ConsumerImpl.builder()
        .topic("testTopic")
        .clientId("myId")
        .configProps(getTestcontainerProperties())
        .consumer(record -> log.info(String.format("consumed message %s:%s", record.key(), record.value())))
        .build();

    final Properties configProps = testee.getConfigProps();
    assertEquals(10, configProps.size());
    assertTrue(configProps.containsKey(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG));
    assertTrue(configProps.containsKey(Config.SCHEMA_SERVER_CONFIG_KEY));
    assertTrue(configProps.containsKey(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG));
    assertTrue(configProps.containsKey(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG));
    assertTrue(configProps.containsKey(ConsumerConfig.GROUP_ID_CONFIG));
    assertTrue(configProps.containsKey(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG));
    assertTrue(configProps.containsKey(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG));
    assertTrue(configProps.containsValue("myId"));
  }
}

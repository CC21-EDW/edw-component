package com.baloise.open.edw.infrastructure.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class ProducerTest {

/*
  public static void main(String[] args) throws ExecutionException, InterruptedException {
    final String topic = "CC21_EDW_1140";
    final Producer producer = new Producer(new Properties(), topic, "stravaConnect");
    producer.pushEvent(topic, "MyKey", "Hello World 3 ").get();
  }
*/

  @Container
  public static KafkaContainer kafkaTestContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.3.2"));

  @Test
  void testEnvironmentReady() {
    final String bootstrapServers = kafkaTestContainer.getBootstrapServers();
    assertNotNull(bootstrapServers);
    assertTrue(bootstrapServers.contains("localhost"));
  }

  @Test
  void verifyInit() throws ExecutionException, InterruptedException {

    Producer testee = new Producer(getTestcontainerProperties(), "testTopic", "myId");

    final Properties configProps = testee.getConfigProps();
    assertEquals(9, configProps.size());
    assertTrue(configProps.containsKey("key.serializer"));
    assertTrue(configProps.containsKey("key.deserializer"));
    assertTrue(configProps.containsKey("value.serializer"));
    assertTrue(configProps.containsKey("value.deserializer"));
    assertTrue(configProps.containsKey(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG));
    assertTrue(configProps.containsKey(ConsumerConfig.GROUP_ID_CONFIG));
    assertTrue(configProps.containsValue("myId"));

  }

  private Properties getTestcontainerProperties() {
    Properties props = new Properties();
    props.put(Config.KAFKA_SERVER_CONFIG_KEY, kafkaTestContainer.getBootstrapServers());
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaTestContainer.getBootstrapServers());
    return props;
  }

}

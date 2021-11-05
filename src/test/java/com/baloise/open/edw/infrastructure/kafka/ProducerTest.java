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
    final String topic = "lz.edw.strava-connect.activity";
    final Producer producer = Producer.create(new Properties(), topic, "SportsRepoTest");
    producer.pushEvent( "I was here!").get();
  }
*/

    @Container
    public static KafkaContainer kafkaTestContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.3.2"));

    @Test
    void verifyInit() throws ExecutionException, InterruptedException {

        ProducerImpl testee = new ProducerImpl(getTestcontainerProperties(), "testTopic", "myId");

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

    private Properties getTestcontainerProperties() {
        final Properties props = new Properties();
        props.put(Config.KAFKA_SERVER_CONFIG_KEY, kafkaTestContainer.getBootstrapServers());
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaTestContainer.getBootstrapServers());
        return props;
    }

}

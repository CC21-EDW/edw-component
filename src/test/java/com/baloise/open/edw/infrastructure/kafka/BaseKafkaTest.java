package com.baloise.open.edw.infrastructure.kafka;


import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Properties;

@Testcontainers
public class BaseKafkaTest {

  @Container
  protected static KafkaContainer kafkaTestContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.3.2"))
      .withEmbeddedZookeeper();


  protected Properties getTestcontainerProperties() {
    final Properties props = new Properties();
    props.put(Config.KAFKA_SERVER_CONFIG_KEY, kafkaTestContainer.getBootstrapServers());
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaTestContainer.getBootstrapServers());
    /*
     * Thx to his blog: https://kreuzwerker.de/post/testing-a-kafka-consumer-with-avro-schema-messages-in-your-spring-boot
     *
     * Hidden in Confluent’s schema registry package, in the AbstractKafkaAvroSerDeConfig class,
     * you can find this comment for the schema registry url:
     *
     * Comma-separated list of URLs for schema registry instances that can be used to register or look up schemas.
     * If you wish to get a connection to a mocked schema registry for testing, you can specify a scope using
     * the ‘mock://’ pseudo-protocol.
     *
     * For example, ‘mock://my-scope-name’ corresponds to ‘MockSchemaRegistry.getClientForScope(“my-scope-name”)’.
     */
    props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "mock://producerTest_init");
    props.put(ProducerConfig.CLIENT_ID_CONFIG, "producerTest_init");
    return props;
  }
}

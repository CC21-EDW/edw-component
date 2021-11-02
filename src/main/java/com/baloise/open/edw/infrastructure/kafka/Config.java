package com.baloise.open.edw.infrastructure.kafka;

import lombok.AccessLevel;
import lombok.Getter;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

public abstract class Config {

  /**
   * In Kafka client the parameter is wrong
   * @{@link AdminClientConfig#BOOTSTRAP_SERVERS_CONFIG} is set to "bootstrap.servers" which does not work
   */
  public static String KAFKA_SERVER_CONFIG_KEY = "kafka.bootstrap.servers";

  private static final Logger LOGGER = LoggerFactory.getLogger("APPL." + MethodHandles.lookup().lookupClass());

  @Getter(AccessLevel.PACKAGE)
  private final Properties configProps = new Properties();

  @Getter()
  private final String topic;

  @Getter
  private final String clientId;

  Config(Properties configProps, String topic, String clientId){
    this.topic = topic;
    this.clientId = clientId;
    initDefaultProps(this.configProps);
    this.configProps.putAll(configProps);
    this.configProps.forEach((key, value) -> LOGGER.debug("{}:{}", key, value));
  }

  private void initDefaultProps(Properties configProps) {
    configProps.put(KAFKA_SERVER_CONFIG_KEY, "localhost:9092"); // comma separated
    configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // comma separated
    configProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    configProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    configProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    configProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    configProps.put(ConsumerConfig.GROUP_ID_CONFIG, getClientId());
    configProps.put("acks", "all");

    try {
      // TODO: set host address instead of localhost
      configProps.put(AdminClientConfig.CLIENT_ID_CONFIG, InetAddress.getLocalHost().getHostName());
    } catch (UnknownHostException e) {
      LOGGER.error(e.getMessage(), e);
    }
  }

}

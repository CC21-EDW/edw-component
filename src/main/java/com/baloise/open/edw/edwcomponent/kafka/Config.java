package com.baloise.open.edw.edwcomponent.kafka;

import lombok.AccessLevel;
import lombok.Getter;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

public abstract class Config {

  private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

  @Getter(AccessLevel.PACKAGE)
  private final Properties configProps = new Properties();

  @Getter()
  private final String topic;

  Config(Properties configProps, String topic){
    this.topic = topic;
    initDefaultProps(this.configProps);
    this.configProps.putAll(configProps);
  }

  private void initDefaultProps(Properties configProps) {
    configProps.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // comma separated
    configProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    configProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    configProps.put("acks", "all");

    try {
      configProps.put(AdminClientConfig.CLIENT_ID_CONFIG, InetAddress.getLocalHost().getHostName());
    } catch (UnknownHostException e) {
      logger.error(e.getMessage(), e);
    }
  }

}

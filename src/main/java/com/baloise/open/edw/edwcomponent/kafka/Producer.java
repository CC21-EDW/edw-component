package com.baloise.open.edw.edwcomponent.kafka;

import lombok.AccessLevel;
import lombok.Getter;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Producer {

  private static final Logger logger = LoggerFactory.getLogger(Producer.class);
  @Getter(AccessLevel.PACKAGE)
  private final Properties configProps = new Properties();
  @Getter()
  private final String topic;

  public Producer(Properties configProps, String topic) throws ExecutionException, InterruptedException {
    this.topic = topic;
    initDefaultProps(this.configProps);
    this.configProps.putAll(configProps);
    initTopic(Admin.create(this.configProps));
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

  private void initTopic(final Admin admin) throws ExecutionException, InterruptedException {
    final Set<String> names = admin.listTopics().names().getNow(Collections.singleton(topic));
    final boolean isTopicInexistent = names.stream().noneMatch(name -> name.equals(topic));

    if (!isTopicInexistent) {
      logger.debug("Topic '{}' exists, skip creation.", topic);
      return;
    }

    final NewTopic newTopic = new NewTopic(topic, 1, (short) 1);
    admin.createTopics(Collections.singleton(newTopic)).values().get(topic).get();
    logger.info("Created topic '{}'", topic);
  }

  public Future<RecordMetadata> pushEventAsychronous(String topic, String key, String value) {
    final ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
    return new KafkaProducer<String, String>(this.configProps).send(record);
  }

}

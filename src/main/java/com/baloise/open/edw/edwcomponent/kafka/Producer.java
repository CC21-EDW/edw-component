package com.baloise.open.edw.edwcomponent.kafka;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Producer extends Config {

  private static final Logger logger = LoggerFactory.getLogger(Producer.class);


  public Producer(Properties configProps, String topic) throws ExecutionException, InterruptedException {
    super(configProps, topic);
    initTopic(Admin.create(getConfigProps()));
  }

  private void initTopic(final Admin admin) throws ExecutionException, InterruptedException {
    final Set<String> names = admin.listTopics().names().getNow(Collections.singleton(getTopic()));
    final boolean isTopicInexistent = names.stream().noneMatch(name -> name.equals(getTopic()));

    if (!isTopicInexistent) {
      logger.debug("Topic '{}' exists, skip creation.", getTopic());
      return;
    }

    final NewTopic newTopic = new NewTopic(getTopic(), 1, (short) 1);
    admin.createTopics(Collections.singleton(newTopic)).values().get(getTopic()).get();
    logger.info("Created topic '{}'", getTopic());
  }

  public Future<RecordMetadata> pushEvent(String topic, String key, String value) {
    final ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
    return new KafkaProducer<String, String>(getConfigProps()).send(record);
  }

}

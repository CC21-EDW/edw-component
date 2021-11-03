package com.baloise.open.edw.infrastructure.kafka;

import com.baloise.open.edw.domain.kafka.Status;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Collections;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Producer extends Config {

  public Producer(Properties configProps, String topic, String clientId) throws ExecutionException, InterruptedException {
    super(configProps, topic, clientId);
    initTopic(Admin.create(getConfigProps()));
  }

  private void initTopic(final Admin admin) throws ExecutionException, InterruptedException {
    isCreateMissingTopic(admin, STAUTS_TOPIC_NAME);
    registerProducer(isCreateMissingTopic(admin, getTopic()));
  }

  private void registerProducer(boolean isNewTopic) {
    if (isNewTopic) {
      Status status = new Status(getClientId(), getTopic(), Status.EventType.TOPIC_CREATED);
      pushEvent(STAUTS_TOPIC_NAME, Status.EventType.TOPIC_CREATED.name(), status.toJson());
    }

    Status status = new Status(getClientId(), getTopic(), Status.EventType.CONNECT);
    pushEvent(STAUTS_TOPIC_NAME, Status.EventType.CONNECT.name(), status.toJson());
  }

  private boolean isCreateMissingTopic(Admin admin, String topicName) throws ExecutionException, InterruptedException {
    final Set<String> names = admin.listTopics().names().get();
    final boolean isTopicInexistent = names.stream().noneMatch(name -> name.equals(topicName));

    if (!isTopicInexistent) {
      LOGGER.debug("Topic '{}' exists, skip creation.", topicName);
      return false;
    }

    final NewTopic newTopic = new NewTopic(topicName, 1, (short) 1);
    admin.createTopics(Collections.singleton(newTopic)).values().get(topicName).get();
    LOGGER.info("Created topic '{}'", topicName);
    return true;
  }

  public Future<RecordMetadata> pushEvent(String topic, String key, String value) {
    final ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, key, value);
    try (final KafkaProducer<String, String> producer = new KafkaProducer<>(getConfigProps())) {
      return producer.send(producerRecord);
    }
  }

}

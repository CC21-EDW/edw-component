package com.baloise.open.edw.infrastructure.kafka;

import com.baloise.open.edw.domain.kafka.Status;
import com.baloise.open.edw.domain.services.CorrelationId;
import com.baloise.open.edw.infrastructure.kafka.mapper.StatusDtoMapper;
import com.baloise.open.edw.infrastructure.kafka.model.StatusDto;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
public class ProducerImpl extends AbstractWorkflow implements Producer {

    ProducerImpl(Properties configProps, String topic, String clientId) throws ExecutionException, InterruptedException {
        super(configProps, topic, clientId);
        initTopic(Admin.create(getConfigProps()));
    }

    private void initTopic(final Admin admin) throws ExecutionException, InterruptedException {
        isCreateMissingTopic(admin, STATUS_TOPIC_NAME);
        registerProducer(isCreateMissingTopic(admin, getTopic()));
    }

    private void registerProducer(boolean isNewTopic) {
        if (isNewTopic) {
            pushStatusTopicCreated();
        }
        pushStatusProducerConnected();
    }

    private boolean isCreateMissingTopic(Admin admin, String topicName) throws ExecutionException, InterruptedException {
        final Set<String> names = admin.listTopics().names().get();
        final boolean isTopicInexistent = names.stream().noneMatch(name -> name.equals(topicName));

        if (!isTopicInexistent) {
            log.debug("Topic '{}' exists, skip creation.", topicName);
            return false;
        }

        final NewTopic newTopic = new NewTopic(topicName, 1, (short) 1);
        admin.createTopics(Collections.singleton(newTopic)).values().get(topicName).get();
        log.info("Created topic '{}'", topicName);
        return true;
    }

    @Override
    public Future<RecordMetadata> pushEvent(String correlationId, Object value) {
        if (correlationId == null || correlationId.isBlank()) {
            throw new IllegalArgumentException("Correlation ID required.");
        }
        return pushEvent(getTopic(), correlationId, value);
    }

    @Override
    public Future<RecordMetadata> pushEvent(Object value) {
        return pushEvent(getTopic(), generateDefaultCorrelationId(), value);
    }

    private Future<RecordMetadata> pushEvent(String inTopicName, String correlationId, Object value) {
        CorrelationId.set(correlationId);

        final ProducerRecord<String, Object> producerRecord = new ProducerRecord<>(inTopicName, correlationId, value);
        try (final KafkaProducer<String, Object> producer = new KafkaProducer<>(getConfigProps())) {
            return producer.send(producerRecord);
        }
    }

    void pushStatusEvent(Status status) {
        pushEvent(STATUS_TOPIC_NAME, generateDefaultCorrelationId(), StatusDtoMapper.INSTANCE.map(status));
    }
}

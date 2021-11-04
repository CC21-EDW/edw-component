package com.baloise.open.edw.infrastructure.kafka;

import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public interface Producer extends Workflow {

    static Producer create(Properties configProps, String topic, String clientId) throws ExecutionException, InterruptedException {
        return new ProducerImpl(configProps, topic, clientId);
    }

    /**
     * Pushes an event to the given topic
     */
    Future<RecordMetadata> pushEvent(String correlationId, Object value);

    /**
     * Pushes an event to the given topic
     */
    Future<RecordMetadata> pushEvent(Object value);
}

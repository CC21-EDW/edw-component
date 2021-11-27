package com.baloise.open.edw.infrastructure.kafka;

import com.baloise.open.edw.domain.kafka.Status;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

@Slf4j
abstract class AbstractWorkflow extends Config implements Workflow{

  AbstractWorkflow(Properties configProps, String topic, String clientId) {
    super(configProps, topic, clientId);
  }

  @Override
  public void pushStatusShutdown() {
    final Status status = new Status(getClientId(), getTopic(), Status.EventType.SHUTDOWN);
    pushStatusEvent(status);
    log.info("Disconnected producer with ID '{}' from workflow.", getClientId());
  }

  @Override
  public void pushStatusTopicCreated() {
    final Status status = new Status(getClientId(), getTopic(), Status.EventType.TOPIC_CREATED);
    pushStatusEvent(status);
    log.info("Created topic with name '{}' by producer with ID '{}' from workflow.", getTopic(), getClientId());
  }

  /**
   * Creates a status event in event topic {@link Config#STATUS_TOPIC_NAME} when producer connects
   */
  void pushStatusConnected() {
    final Status status = new Status(getClientId(), getTopic(), Status.EventType.CONNECT);
    pushStatusEvent(status);
    log.info("Connected producer with ID '{}' to workflow.", getClientId());
  }

  abstract void pushStatusEvent(Status status);
}

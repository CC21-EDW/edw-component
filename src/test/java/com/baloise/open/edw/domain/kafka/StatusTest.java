package com.baloise.open.edw.domain.kafka;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusTest {

  @Test
  void name() {
    Status testee = new Status("myClientId", "myTopicName", Status.EventType.SHUTDOWN);
    assertNotNull(testee);
    assertEquals("myClientId", testee.getClientId());
    assertEquals("myTopicName", testee.getTopic());
    assertEquals(Status.EventType.SHUTDOWN, testee.getEventType());
  }
}

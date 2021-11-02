package com.baloise.open.edw.edwcomponent.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProducerTest {

  @Test
  @Disabled("until Testcontainer is setup (#3)")
  void verifyInit() throws ExecutionException, InterruptedException {
    Producer testee = new Producer(new Properties(), "testTopic");

    final Properties configProps = testee.getConfigProps();
    assertEquals(5, configProps.size());
    assertTrue(configProps.containsKey("key.serializer"));
    assertTrue(configProps.containsKey("value.serializer"));
    assertTrue(configProps.containsKey(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG));
  }
}

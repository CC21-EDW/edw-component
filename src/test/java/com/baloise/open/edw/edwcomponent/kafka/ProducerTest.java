package com.baloise.open.edw.edwcomponent.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProducerTest {

  public static void main(String[] args) {

  }

  @Test
  @Disabled("until Testcontainer is setup (#3)")
  void verifyInit() throws ExecutionException, InterruptedException {
    Producer testee = new Producer(new Properties(), "testTopic", "myId");

    final Properties configProps = testee.getConfigProps();
    assertEquals(8, configProps.size());
    assertTrue(configProps.containsKey("key.serializer"));
    assertTrue(configProps.containsKey("key.deserializer"));
    assertTrue(configProps.containsKey("value.serializer"));
    assertTrue(configProps.containsKey("value.deserializer"));
    assertTrue(configProps.containsKey(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG));
    assertTrue(configProps.containsKey(ConsumerConfig.GROUP_ID_CONFIG));
    assertTrue(configProps.containsValue("myId"));
  }
}

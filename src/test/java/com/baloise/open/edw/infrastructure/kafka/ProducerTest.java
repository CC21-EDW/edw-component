package com.baloise.open.edw.infrastructure.kafka;

import com.baloise.open.edw.infrastructure.kafka.Producer;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProducerTest {

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    final Producer producer = new Producer(new Properties(), "CC21_EDW_2128", "stravaConnect");
    producer.pushEvent("CC21_EDW_2128", "MyKey", "Hello World 2 ").get();
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
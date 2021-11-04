package com.baloise.open.edw.infrastructure.kafka;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class ConsumerTest  {

  public static void main(String[] args) {
    final ConsumerImpl testee = ConsumerImpl.builder()
        .topic("CC21_EDW_2128")
        .clientId("myTestClient")
        .consumer(record -> log.info(String.format("consumed message %s:%s", record.key(), record.value())))
        .build();

    testee.run();
  }

}

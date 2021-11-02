package com.baloise.open.edw.domain.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CorelationIdTest {

  @Test
  void name() {
    CorelationId.set("TEST123");
    assertEquals("TEST123", CorelationId.get());
  }
}

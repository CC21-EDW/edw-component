package com.baloise.open.edw.domain.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CorrelationIdTest {

  @Test
  void name() {
    CorrelationId.set("TEST123");
    assertEquals("TEST123", CorrelationId.get());
  }
}

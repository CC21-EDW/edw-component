package com.baloise.open.edw.domain.services;

import org.slf4j.MDC;

import java.util.HashMap;

public class CorrelationId {

  private static final String PREFIX = "CORRELATION-ID";

  private CorrelationId() {
    // do not instantiate
    if (MDC.getCopyOfContextMap() == null) {
      MDC.setContextMap(new HashMap<>());
    }
  }

  public static String get() {
    return MDC.get(PREFIX);
  }

  public static void set(String correlationId) {
    if (correlationId == null) {
      MDC.remove(PREFIX);
    } else {
      MDC.put(PREFIX, correlationId);
    }
  }

}

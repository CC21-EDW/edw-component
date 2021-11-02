package com.baloise.open.edw.domain.services;

import org.slf4j.MDC;

import java.util.HashMap;

public class CorelationId {

  private static final String PREFIX = "CORELATION-ID";

  private CorelationId() {
    // do not instantiate
    if (MDC.getCopyOfContextMap() == null) {
      MDC.setContextMap(new HashMap<>());
    }
  }

  public static String get() {
    return MDC.get(PREFIX);
  }

  public static void set(String corelationId) {
    if (corelationId == null) {
      MDC.remove(PREFIX);
    } else {
      MDC.put(PREFIX, corelationId);
    }
  }

}

package com.baloise.open.edw.domain.services;

import org.slf4j.MDC;

public class CorelationId {

  private static final String PREFIX = "CORELATION-ID";

  private CorelationId() {
    // do not instantiate
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

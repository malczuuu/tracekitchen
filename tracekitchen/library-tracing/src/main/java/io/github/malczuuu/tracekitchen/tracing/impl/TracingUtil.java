package io.github.malczuuu.tracekitchen.tracing.impl;

import java.util.UUID;

final class TracingUtil {

  static String generateId() {
    return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
  }

  private TracingUtil() {}
}

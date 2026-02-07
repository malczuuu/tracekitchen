package io.github.malczuuu.tracekitchen.tracing.core;

import java.util.UUID;

final class TraceFactoryImpl implements TraceFactory {

  static final TraceFactoryImpl INSTANCE = new TraceFactoryImpl();

  TraceFactoryImpl() {}

  @Override
  public String makeTraceId() {
    return generate();
  }

  @Override
  public String makeSpanId() {
    return generate().substring(0, 16);
  }

  private String generate() {
    return UUID.randomUUID().toString().replaceAll("-", "");
  }
}

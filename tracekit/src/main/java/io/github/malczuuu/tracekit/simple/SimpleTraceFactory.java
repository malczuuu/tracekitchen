package io.github.malczuuu.tracekit.simple;

import io.github.malczuuu.tracekit.TraceFactory;
import java.util.UUID;

final class SimpleTraceFactory implements TraceFactory {

  static TraceFactory getInstance() {
    return Holder.INSTANCE;
  }

  private SimpleTraceFactory() {}

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

  private static final class Holder {
    static final SimpleTraceFactory INSTANCE = new SimpleTraceFactory();
  }
}

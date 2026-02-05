package io.github.malczuuu.tracekitchen.tracing.impl;

import static io.github.malczuuu.tracekitchen.tracing.impl.TracingUtil.generateId;

import io.github.malczuuu.tracekitchen.tracing.api.Context;

final class SimpleContext implements Context {

  private final String traceId;
  private final String spanId;
  private final String parentSpanId;

  SimpleContext() {
    this(generateId(), generateId());
  }

  SimpleContext(String traceId, String spanId) {
    this(traceId, spanId, null);
  }

  SimpleContext(String traceId, String spanId, String parentSpanId) {
    this.traceId = traceId;
    this.spanId = spanId;
    this.parentSpanId = parentSpanId;
  }

  @Override
  public Context makeChild() {
    return new SimpleContext(traceId, generateId(), spanId);
  }

  @Override
  public String getTraceId() {
    return traceId;
  }

  @Override
  public String getSpanId() {
    return spanId;
  }

  @Override
  public String getParentSpanId() {
    return parentSpanId;
  }
}

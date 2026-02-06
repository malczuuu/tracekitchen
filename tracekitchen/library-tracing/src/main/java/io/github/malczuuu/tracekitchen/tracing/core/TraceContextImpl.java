package io.github.malczuuu.tracekitchen.tracing.core;

import org.jspecify.annotations.Nullable;

final class TraceContextImpl implements TraceContext {

  private final String traceId;
  private final String spanId;
  private final @Nullable String parentSpanId;

  private final TraceFactory traceFactory;

  TraceContextImpl(TraceFactory traceFactory) {
    this(traceFactory.makeTraceId(), traceFactory.makeSpanId(), null, traceFactory);
  }

  TraceContextImpl(
      String traceId, String spanId, @Nullable String parentSpanId, TraceFactory traceFactory) {
    this.traceId = traceId;
    this.spanId = spanId;
    this.parentSpanId = parentSpanId;
    this.traceFactory = traceFactory;
  }

  @Override
  public TraceContext makeChild() {
    return new TraceContextImpl(traceId, traceFactory.makeSpanId(), spanId, traceFactory);
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
  public @Nullable String getParentSpanId() {
    return parentSpanId;
  }
}

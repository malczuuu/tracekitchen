package io.github.malczuuu.tracekitchen.tracing.core;

import org.jspecify.annotations.Nullable;

final class TraceContextBuilderImpl implements TraceContextBuilder {

  private final @Nullable String traceId;
  private final @Nullable String spanId;
  private final @Nullable String parentSpanId;

  private final TraceFactory traceFactory;

  TraceContextBuilderImpl(TraceFactory traceFactory) {
    this(null, null, null, traceFactory);
  }

  TraceContextBuilderImpl(
      @Nullable String traceId,
      @Nullable String spanId,
      @Nullable String parentSpanId,
      TraceFactory traceFactory) {
    this.traceId = traceId;
    this.spanId = spanId;
    this.parentSpanId = parentSpanId;
    this.traceFactory = traceFactory;
  }

  @Override
  public TraceContextBuilderImpl withTraceId(String traceId) {
    return new TraceContextBuilderImpl(traceId, this.spanId, this.parentSpanId, traceFactory);
  }

  @Override
  public TraceContextBuilderImpl withSpanId(String spanId) {
    return new TraceContextBuilderImpl(this.traceId, spanId, this.parentSpanId, traceFactory);
  }

  @Override
  public TraceContextBuilderImpl withParentSpanId(String parentSpanId) {
    return new TraceContextBuilderImpl(this.traceId, this.spanId, parentSpanId, traceFactory);
  }

  @Override
  public TraceContext build() {
    return new TraceContextImpl(evalTraceId(), evalSpanId(), parentSpanId, traceFactory);
  }

  private String evalTraceId() {
    return hasText(this.traceId) ? this.traceId.trim() : traceFactory.makeTraceId();
  }

  private String evalSpanId() {
    return hasText(this.traceId) && hasText(this.spanId)
        ? this.spanId.trim()
        : traceFactory.makeSpanId();
  }

  public boolean hasText(@Nullable String str) {
    return str != null && !str.isBlank();
  }
}

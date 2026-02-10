package io.github.malczuuu.tracekitchen;

import java.time.Clock;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

final class TraceContextBuilderImpl implements TraceContextBuilder {

  private final @Nullable String name;
  private final @Nullable String traceId;
  private final @Nullable String spanId;
  private final @Nullable String parentSpanId;

  private final Clock clock;
  private final TraceContextLifecycleAdapter lifecycleAdapter;
  private final TraceFactory traceFactory;

  TraceContextBuilderImpl(
      Clock clock, TraceContextLifecycleAdapter lifecycleAdapter, TraceFactory traceFactory) {
    this(null, null, null, null, clock, lifecycleAdapter, traceFactory);
  }

  TraceContextBuilderImpl(
      @Nullable String name,
      @Nullable String traceId,
      @Nullable String spanId,
      @Nullable String parentSpanId,
      Clock clock,
      TraceContextLifecycleAdapter lifecycleAdapter,
      TraceFactory traceFactory) {
    this.name = name;
    this.traceId = traceId;
    this.spanId = spanId;
    this.parentSpanId = parentSpanId;
    this.clock = clock;
    this.lifecycleAdapter = lifecycleAdapter;
    this.traceFactory = traceFactory;
  }

  @Override
  public TraceContextBuilder withName(String name) {
    return new TraceContextBuilderImpl(
        name, traceId, spanId, parentSpanId, clock, lifecycleAdapter, traceFactory);
  }

  @Override
  public TraceContextBuilderImpl withTraceId(String traceId) {
    return new TraceContextBuilderImpl(
        name, traceId, spanId, parentSpanId, clock, lifecycleAdapter, traceFactory);
  }

  @Override
  public TraceContextBuilderImpl withSpanId(String spanId) {
    return new TraceContextBuilderImpl(
        name, traceId, spanId, parentSpanId, clock, lifecycleAdapter, traceFactory);
  }

  @Override
  public TraceContextBuilderImpl withParentSpanId(String parentSpanId) {
    return new TraceContextBuilderImpl(
        name, traceId, spanId, parentSpanId, clock, lifecycleAdapter, traceFactory);
  }

  @Override
  public boolean isComplete() {
    return hasText(traceId) && hasText(spanId);
  }

  @Override
  public TraceContext build() {
    if (isComplete()) {
      Objects.requireNonNull(traceId);
      Objects.requireNonNull(spanId);
      return new TraceContextImpl(
          name, traceId, spanId, parentSpanId, clock, lifecycleAdapter, traceFactory);
    } else {
      return new TraceContextImpl(
          name,
          traceFactory.makeTraceId(),
          traceFactory.makeSpanId(),
          null,
          clock,
          lifecycleAdapter,
          traceFactory);
    }
  }

  public boolean hasText(@Nullable String str) {
    return str != null && !str.isBlank();
  }
}

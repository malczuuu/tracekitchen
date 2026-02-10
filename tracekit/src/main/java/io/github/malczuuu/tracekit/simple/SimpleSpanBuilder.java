package io.github.malczuuu.tracekit.simple;

import io.github.malczuuu.tracekit.Span;
import io.github.malczuuu.tracekit.SpanBuilder;
import io.github.malczuuu.tracekit.SpanLifecycleAdapter;
import io.github.malczuuu.tracekit.TraceFactory;
import java.time.Clock;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

final class SimpleSpanBuilder implements SpanBuilder {

  private final @Nullable String name;
  private final @Nullable String traceId;
  private final @Nullable String spanId;
  private final @Nullable String parentSpanId;
  private final boolean sampled;

  private final Clock clock;
  private final SpanLifecycleAdapter lifecycleAdapter;
  private final TraceFactory traceFactory;

  SimpleSpanBuilder(Clock clock, SpanLifecycleAdapter lifecycleAdapter, TraceFactory traceFactory) {
    this(null, null, null, null, false, clock, lifecycleAdapter, traceFactory);
  }

  SimpleSpanBuilder(
      @Nullable String name,
      @Nullable String traceId,
      @Nullable String spanId,
      @Nullable String parentSpanId,
      boolean sampled,
      Clock clock,
      SpanLifecycleAdapter lifecycleAdapter,
      TraceFactory traceFactory) {
    this.name = name;
    this.traceId = traceId;
    this.spanId = spanId;
    this.parentSpanId = parentSpanId;
    this.sampled = sampled;
    this.clock = clock;
    this.lifecycleAdapter = lifecycleAdapter;
    this.traceFactory = traceFactory;
  }

  @Override
  public SpanBuilder withName(String name) {
    return new SimpleSpanBuilder(
        name, traceId, spanId, parentSpanId, sampled, clock, lifecycleAdapter, traceFactory);
  }

  @Override
  public SimpleSpanBuilder withTraceId(String traceId) {
    return new SimpleSpanBuilder(
        name, traceId, spanId, parentSpanId, sampled, clock, lifecycleAdapter, traceFactory);
  }

  @Override
  public SimpleSpanBuilder withSpanId(String spanId) {
    return new SimpleSpanBuilder(
        name, traceId, spanId, parentSpanId, sampled, clock, lifecycleAdapter, traceFactory);
  }

  @Override
  public SimpleSpanBuilder withParentSpanId(String parentSpanId) {
    return new SimpleSpanBuilder(
        name, traceId, spanId, parentSpanId, sampled, clock, lifecycleAdapter, traceFactory);
  }

  @Override
  public SimpleSpanBuilder withSampled(boolean sampled) {
    return new SimpleSpanBuilder(
        name, traceId, spanId, parentSpanId, sampled, clock, lifecycleAdapter, traceFactory);
  }

  @Override
  public boolean isComplete() {
    return hasText(traceId) && hasText(spanId);
  }

  @Override
  public Span build() {
    if (isComplete()) {
      Objects.requireNonNull(traceId);
      Objects.requireNonNull(spanId);
      return new SimpleSpan(
          name,
          new SimpleTrace(traceId, spanId, parentSpanId, sampled),
          clock,
          lifecycleAdapter,
          traceFactory);
    } else {
      return new SimpleSpan(
          name,
          new SimpleTrace(traceFactory.makeTraceId(), traceFactory.makeSpanId(), null, sampled),
          clock,
          lifecycleAdapter,
          traceFactory);
    }
  }

  private static boolean hasText(@Nullable String str) {
    return str != null && !str.isBlank();
  }
}

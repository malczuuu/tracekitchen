package io.github.malczuuu.tracekitchen.tracing.core;

import java.time.Clock;
import org.jspecify.annotations.Nullable;

public class SimpleTracerBuilder {

  private final @Nullable TraceFactory traceFactory;
  private final @Nullable LoggingContextAdapter loggingContextAdapter;
  private final @Nullable ContextLifecycleAdapter contextLifecycleAdapter;
  private final @Nullable Clock clock;

  public SimpleTracerBuilder() {
    this(null, null, null, null);
  }

  private SimpleTracerBuilder(
      @Nullable TraceFactory traceFactory,
      @Nullable LoggingContextAdapter loggingContextAdapter,
      @Nullable ContextLifecycleAdapter contextLifecycleAdapter,
      @Nullable Clock clock) {
    this.traceFactory = traceFactory;
    this.loggingContextAdapter = loggingContextAdapter;
    this.contextLifecycleAdapter = contextLifecycleAdapter;
    this.clock = clock;
  }

  public SimpleTracerBuilder withTraceFactory(@Nullable TraceFactory traceFactory) {
    return new SimpleTracerBuilder(
        traceFactory, loggingContextAdapter, contextLifecycleAdapter, clock);
  }

  public SimpleTracerBuilder withLoggingContextAdapter(
      @Nullable LoggingContextAdapter loggingContextAdapter) {
    return new SimpleTracerBuilder(
        traceFactory, loggingContextAdapter, contextLifecycleAdapter, clock);
  }

  public SimpleTracerBuilder withContextLifecycleAdapter(
      @Nullable ContextLifecycleAdapter contextLifecycleAdapter) {
    return new SimpleTracerBuilder(
        traceFactory, loggingContextAdapter, contextLifecycleAdapter, clock);
  }

  public SimpleTracerBuilder withClock(@Nullable Clock clock) {
    return new SimpleTracerBuilder(
        traceFactory, loggingContextAdapter, contextLifecycleAdapter, clock);
  }

  public SimpleTracer build() {
    TraceFactory traceFactory =
        this.traceFactory != null ? this.traceFactory : SimpleTraceFactory.getInstance();
    LoggingContextAdapter loggingContextAdapter =
        this.loggingContextAdapter != null
            ? this.loggingContextAdapter
            : NoOpLoggingAdapter.getInstance();
    ContextLifecycleAdapter contextLifecycleAdapter =
        this.contextLifecycleAdapter != null
            ? this.contextLifecycleAdapter
            : NoOpLifecycleAdapter.getInstance();
    Clock clock = this.clock != null ? this.clock : Clock.systemUTC();
    return new SimpleTracer(traceFactory, loggingContextAdapter, contextLifecycleAdapter, clock);
  }
}

package io.github.malczuuu.tracekitchen;

import java.time.Clock;
import org.jspecify.annotations.Nullable;

/** Builder for creating instances of {@link SimpleTracer}. */
public class SimpleTracerBuilder {

  private final @Nullable TraceFactory traceFactory;
  private final @Nullable LoggingContextAdapter loggingContextAdapter;
  private final @Nullable ContextLifecycleAdapter contextLifecycleAdapter;
  private final @Nullable Clock clock;

  /** Creates new {@link SimpleTracerBuilder} object. */
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

  /**
   * Sets the {@link TraceFactory} to be used by the tracer.
   *
   * @param traceFactory the {@link TraceFactory} to use, or {@code null} to use the default
   * @return a new builder instance with the given trace factory
   */
  public SimpleTracerBuilder withTraceFactory(@Nullable TraceFactory traceFactory) {
    return new SimpleTracerBuilder(
        traceFactory, loggingContextAdapter, contextLifecycleAdapter, clock);
  }

  /**
   * Sets the {@link LoggingContextAdapter} to integrate with MDC.
   *
   * @param loggingContextAdapter the logging adapter to use, or {@code null} to use default
   * @return a new builder instance with the given logging context adapter
   */
  public SimpleTracerBuilder withLoggingContextAdapter(
      @Nullable LoggingContextAdapter loggingContextAdapter) {
    return new SimpleTracerBuilder(
        traceFactory, loggingContextAdapter, contextLifecycleAdapter, clock);
  }

  /**
   * Sets the {@link ContextLifecycleAdapter} to receive context lifecycle events.
   *
   * @param contextLifecycleAdapter the lifecycle adapter to use, or {@code null} to default
   * @return a new builder instance with the given context lifecycle adapter
   */
  public SimpleTracerBuilder withContextLifecycleAdapter(
      @Nullable ContextLifecycleAdapter contextLifecycleAdapter) {
    return new SimpleTracerBuilder(
        traceFactory, loggingContextAdapter, contextLifecycleAdapter, clock);
  }

  /**
   * Sets the {@link Clock} to be used by the tracer.
   *
   * @param clock the clock to use, or {@code null} to use default
   * @return a new builder instance with the given clock
   */
  public SimpleTracerBuilder withClock(@Nullable Clock clock) {
    return new SimpleTracerBuilder(
        traceFactory, loggingContextAdapter, contextLifecycleAdapter, clock);
  }

  /**
   * Builds a new instance of {@link SimpleTracer} using the configured options.
   *
   * @return a fully configured {@link SimpleTracer} instance
   */
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

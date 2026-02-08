package io.github.malczuuu.tracekitchen;

import java.time.Clock;
import org.jspecify.annotations.Nullable;

/** Builder for creating instances of {@link SimpleTracer}. */
public class SimpleTracerBuilder {

  private final @Nullable TraceFactory traceFactory;
  private final CompositeLifecycleAdapter lifecycleAdapter;
  private final @Nullable Clock clock;

  /** Creates new {@link SimpleTracerBuilder} object. */
  public SimpleTracerBuilder() {
    this(null, CompositeLifecycleAdapter.empty(), null);
  }

  private SimpleTracerBuilder(
      @Nullable TraceFactory traceFactory,
      CompositeLifecycleAdapter lifecycleAdapter,
      @Nullable Clock clock) {
    this.traceFactory = traceFactory;
    this.lifecycleAdapter = lifecycleAdapter;
    this.clock = clock;
  }

  /**
   * Sets the {@link TraceFactory} to be used by the tracer.
   *
   * @param traceFactory the {@link TraceFactory} to use, or {@code null} to use the default
   * @return a new builder instance with the given trace factory
   */
  public SimpleTracerBuilder withTraceFactory(@Nullable TraceFactory traceFactory) {
    return new SimpleTracerBuilder(traceFactory, lifecycleAdapter, clock);
  }

  /**
   * Adds the {@link TraceContextLifecycleAdapter} to receive context lifecycle events.
   *
   * @param lifecycleAdapter the lifecycle adapter to add
   * @return a new builder instance with the given context lifecycle adapter
   */
  public SimpleTracerBuilder addLifecycleAdapter(
      @Nullable TraceContextLifecycleAdapter lifecycleAdapter) {
    if (lifecycleAdapter == null) {
      return this;
    }
    return new SimpleTracerBuilder(
        traceFactory, this.lifecycleAdapter.add(lifecycleAdapter), clock);
  }

  /**
   * Sets the {@link Clock} to be used by the tracer.
   *
   * @param clock the clock to use, or {@code null} to use default
   * @return a new builder instance with the given clock
   */
  public SimpleTracerBuilder withClock(@Nullable Clock clock) {
    return new SimpleTracerBuilder(traceFactory, lifecycleAdapter, clock);
  }

  /**
   * Builds a new instance of {@link SimpleTracer} using the configured options.
   *
   * @return a fully configured {@link SimpleTracer} instance
   */
  public SimpleTracer build() {
    TraceFactory traceFactory =
        this.traceFactory != null ? this.traceFactory : SimpleTraceFactory.getInstance();
    Clock clock = this.clock != null ? this.clock : Clock.systemUTC();
    return new SimpleTracer(traceFactory, lifecycleAdapter, clock);
  }
}

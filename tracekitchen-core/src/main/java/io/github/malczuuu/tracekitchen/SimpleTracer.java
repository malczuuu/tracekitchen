package io.github.malczuuu.tracekitchen;

import java.time.Clock;
import org.jspecify.annotations.Nullable;

/** Simple implementation of the {@link Tracer} interface. */
public class SimpleTracer implements Tracer {

  private final TraceFactory traceFactory;
  private final TraceContextLifecycleAdapter lifecycleAdapter;
  private final Clock clock;

  public SimpleTracer(
      TraceFactory traceFactory, TraceContextLifecycleAdapter lifecycleAdapter, Clock clock) {
    this.traceFactory = traceFactory;
    this.lifecycleAdapter = lifecycleAdapter;
    this.clock = clock;
  }

  /**
   * {@inheritDoc}
   *
   * @return a new {@link TraceContext} representing the root span
   */
  @Override
  public TraceContext newRootContext() {
    return new TraceContextImpl(clock, lifecycleAdapter, traceFactory);
  }

  /**
   * {@inheritDoc}
   *
   * @param name the name of the child span; must not be {@code null} or blank
   * @return a new {@link TraceContext} representing the root span
   */
  @Override
  public TraceContext newRootContext(String name) {
    return new TraceContextImpl(name, clock, lifecycleAdapter, traceFactory);
  }

  /**
   * {@inheritDoc}
   *
   * @return a new {@link TraceContextBuilder}
   */
  @Override
  public TraceContextBuilder contextBuilder() {
    return new TraceContextBuilderImpl(clock, lifecycleAdapter, traceFactory);
  }

  /**
   * {@inheritDoc}
   *
   * @return the currently active {@link TraceContext}, or {@code null} if none
   */
  @Override
  public @Nullable TraceContextSnapshot getCurrentContext() {
    return ContextThreadLocalHolder.current();
  }
}

package io.github.malczuuu.tracekit.simple;

import io.github.malczuuu.tracekit.Span;
import io.github.malczuuu.tracekit.SpanBuilder;
import io.github.malczuuu.tracekit.SpanLifecycleAdapter;
import io.github.malczuuu.tracekit.TraceFactory;
import io.github.malczuuu.tracekit.Tracer;
import java.time.Clock;
import org.jspecify.annotations.Nullable;

/** Simple implementation of the {@link Tracer} interface. */
public class SimpleTracer implements Tracer {

  private final TraceFactory traceFactory;
  private final SpanLifecycleAdapter lifecycleAdapter;
  private final Clock clock;

  SimpleTracer(TraceFactory traceFactory, SpanLifecycleAdapter lifecycleAdapter, Clock clock) {
    this.traceFactory = traceFactory;
    this.lifecycleAdapter = lifecycleAdapter;
    this.clock = clock;
  }

  /**
   * {@inheritDoc}
   *
   * @return a new {@link Span} representing the root span
   */
  @Override
  public Span root() {
    return new SpanImpl(clock, lifecycleAdapter, traceFactory);
  }

  /**
   * {@inheritDoc}
   *
   * @param name the name of the child span; must not be {@code null} or blank
   * @return a new {@link Span} representing the root span
   */
  @Override
  public Span root(String name) {
    return new SpanImpl(name, clock, lifecycleAdapter, traceFactory);
  }

  /**
   * {@inheritDoc}
   *
   * @return a new {@link SpanBuilder}
   */
  @Override
  public SpanBuilder builder() {
    return new SpanBuilderImpl(clock, lifecycleAdapter, traceFactory);
  }

  /**
   * {@inheritDoc}
   *
   * @return the currently active {@link Span}, or {@code null} if none
   */
  @Override
  public @Nullable Span getCurrentSpan() {
    return ThreadLocalSpanHolder.current();
  }
}

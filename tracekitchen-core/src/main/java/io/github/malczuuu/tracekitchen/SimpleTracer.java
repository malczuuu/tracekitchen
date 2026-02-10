package io.github.malczuuu.tracekitchen;

import java.time.Clock;
import org.jspecify.annotations.Nullable;

/**
 * Simple implementation of the {@link Tracer} interface.
 *
 * <p>This tracer provides a minimal, thread-safe tracing mechanism suitable for synchronous and
 * asynchronous applications. It supports creating trace contexts, building contexts from incoming
 * data, opening scopes, and automatically managing {@code MDC} values.
 *
 * <h2>Features:</h2>
 *
 * <ul>
 *   <li>Create root or child {@link TraceContext} instances
 *   <li>Build {@link TraceContext} from extracted or custom values
 *   <li>Open a {@link OpenContext} scope using {@code try-with-resources}
 *   <li>Automatically push/pop context to {@code ThreadLocal} stack
 *   <li>Allow propagating context data into MDC or consuming lifecycle events in any other way via
 *       {@link TraceContextLifecycleAdapter}
 * </ul>
 */
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
  public @Nullable TraceContext getCurrentContext() {
    return ContextThreadLocalHolder.current();
  }
}

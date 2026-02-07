package io.github.malczuuu.tracekitchen.tracing.core;

import java.time.Duration;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/**
 * Represents a tracing context, including the current span and its trace.
 *
 * <p>A {@code TraceContext} carries the identifiers used for distributed tracing, typically
 * propagated over HTTP headers, messaging systems, or other inter-service calls.
 *
 * <p>Instances are immutable. Child contexts can be created via {@link #makeChild()}, which
 * inherits the {@link #getTraceId()} but generates a new {@link #getSpanId()} with the current span
 * as parent.
 */
public interface TraceContext {

  /**
   * Creates a child context of this context.
   *
   * <p>The child context inherits the trace ID of this context, and sets this context's span ID as
   * its parent span ID. The child context has a new, unique span ID.
   *
   * @return a new child {@link TraceContext}
   */
  TraceContext makeChild();

  /**
   * Returns the trace identifier for this context.
   *
   * @return non-null trace ID
   */
  String getTraceId();

  /**
   * Returns the span identifier for this context.
   *
   * @return non-null span ID
   */
  String getSpanId();

  /**
   * Returns the parent span identifier, or {@code null} if this is a root span.
   *
   * @return parent span ID, or {@code null} for root
   */
  @Nullable String getParentSpanId();

  void open(Instant time);

  void close(Instant time);

  ContextState getState();

  Duration getDuration();
}

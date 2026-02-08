package io.github.malczuuu.tracekitchen;

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
   * Creates a named child context of this context.
   *
   * <p>The child context inherits the trace ID of this context and sets this context's span ID as
   * its parent span ID. The child context is assigned a new, unique span ID and is associated with
   * the given span name.
   *
   * <p>The provided name is intended for observability and diagnostic purposes (e.g. span or
   * operation naming) and does not affect context propagation.
   *
   * @param name the name of the child span; must not be {@code null} or blank
   * @return a new child {@link TraceContext} with the given name
   */
  TraceContext makeChild(String name);

  /**
   * Returns the name of this context.
   *
   * @return non-null context name ({@code "<anonymous>"} if no name assigned)
   */
  String getName();

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

  /**
   * Opens the context at the specified time.
   *
   * @param time the {@link Instant} when the context is opened
   * @throws IllegalStateException if the context is already opened
   */
  void open(Instant time);

  /**
   * Closes the context at the specified time.
   *
   * @param time the {@link Instant} when the context is closed
   * @throws IllegalStateException if the context is not opened, already closed, or if the specified
   *     time is before the open time
   */
  void close(Instant time);

  /**
   * Returns the current state of the context. State depends on previously called (or not-called)
   * {@link #open(Instant)} and/or {@link #close(Instant)} methods.
   *
   * @return the {@link ContextState} representing the current state
   */
  ContextState getState();

  /**
   * Returns the total duration for which the context has been open.
   *
   * <p>The duration is zero if the context has never been opened or has not yet been closed.
   *
   * @return a {@link Duration} representing the elapsed time between the last {@link
   *     #open(Instant)} and {@link #close(Instant)} calls, or {@link Duration#ZERO} if the context
   *     is not fully opened and closed
   */
  Duration getDuration();
}

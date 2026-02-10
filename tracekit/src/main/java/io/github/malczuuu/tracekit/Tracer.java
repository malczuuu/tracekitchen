package io.github.malczuuu.tracekit;

import org.jspecify.annotations.Nullable;

/**
 * Main entry point for tracing operations.
 *
 * <p>This interface defines the core operations for creating, building, opening, and accessing
 * spans.
 */
public interface Tracer {

  /**
   * Creates a new root span with a new trace ID and span ID, without parent span ID.
   *
   * @return a new {@link Span} representing the root span
   */
  Span root();

  /**
   * Creates a new root span with a new trace ID and span ID, without parent span ID.
   *
   * @param name the name of the child span; must not be {@code null} or blank
   * @return a new {@link Span} representing the root span
   */
  Span root(String name);

  /**
   * Returns a builder for constructing a {@link Span}.
   *
   * <p>Use this to create spans from incoming trace information (headers, messages) or to manually
   * set trace and span IDs.
   *
   * @return a new {@link SpanBuilder}
   */
  SpanBuilder builder();

  /**
   * Returns the currently active span, or {@code null} if none.
   *
   * <p>This reflects the span most recently opened in the current thread (or execution scope in
   * reactive environments).
   *
   * @return the current {@link Span}, or {@code null} if no span is active
   */
  @Nullable Span getCurrentSpan();
}

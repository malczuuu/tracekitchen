package io.github.malczuuu.tracekitchen;

import org.jspecify.annotations.Nullable;

/**
 * Main entry point for tracing operations.
 *
 * <p>This interface defines the core operations for creating, building, opening, and accessing
 * tracing contexts.
 *
 * <pre>{@code
 * // create a root context using builder (e.g., from incoming headers)
 * TraceContext root = tracer.contextBuilder()
 *     .withTraceId(request.headers.xTraceId)
 *     .withSpanId(request.headers.xSpanId)
 *     .withParentSpanId(request.headers.xParentSpanId)
 *     .build();
 *
 * // open the context for the current scope (try-with-resources)
 * try (OpenContext o = tracer.open(root)) {
 *     log.info("This is happening inside root span");
 *
 *     // spawn a child context
 *     TraceContext child = root.makeChild();
 *
 *     try (OpenContext oc = child.open()) {
 *         log.info("This is happening inside child span");
 *         // log can be different from previous one if MDC is supported
 *     }
 * }
 *
 * // outside of any scope, getCurrentContext() will return null
 * TraceContext current = tracer.getCurrentContext();
 * }</pre>
 */
public interface Tracer {

  /**
   * Creates a new root tracing context with a new trace ID and span ID, without parent span ID.
   *
   * @return a new {@link TraceContext} representing the root span
   */
  TraceContext newRootContext();

  /**
   * Creates a new named root tracing context with a new trace ID and span ID, without parent span
   * ID.
   *
   * <p>The provided name is intended for observability and diagnostic purposes (e.g. span or
   * operation naming) and does not affect context propagation.
   *
   * @param name the name of the child span; must not be {@code null} or blank
   * @return a new {@link TraceContext} representing the root span
   */
  TraceContext newRootContext(String name);

  /**
   * Returns a builder for constructing a {@link TraceContext}.
   *
   * <p>Use this to create contexts from incoming trace information (headers, messages) or to
   * manually set trace and span IDs.
   *
   * @return a new {@link TraceContextBuilder}
   */
  TraceContextBuilder contextBuilder();

  /**
   * Returns the currently active tracing context, or {@code null} if none.
   *
   * <p>This reflects the context most recently opened in the current thread (or execution scope in
   * reactive environments).
   *
   * @return the current {@link TraceContextSnapshot}, or {@code null} if no context is active
   */
  @Nullable TraceContextSnapshot getCurrentContext();
}

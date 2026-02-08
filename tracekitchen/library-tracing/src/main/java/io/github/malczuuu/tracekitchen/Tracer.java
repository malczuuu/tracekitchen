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
 *     try (OpenContext oc = tracer.open(child)) {
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
   * Returns a builder for constructing a {@link TraceContext}.
   *
   * <p>Use this to create contexts from incoming trace information (headers, messages) or to
   * manually set trace and span IDs.
   *
   * @return a new {@link TraceContextBuilder}
   */
  TraceContextBuilder contextBuilder();

  /**
   * Opens the given context for the current execution scope.
   *
   * <p>Returns an {@link OpenContext} that should be used in a {@code try-with-resources} block to
   * ensure the previous context is restored on close.
   *
   * @param context the context to make active
   * @return an {@link OpenContext} representing the active scope
   */
  OpenContext open(TraceContext context);

  /**
   * Returns the currently active tracing context, or {@code null} if none.
   *
   * <p>This reflects the context most recently opened in the current thread (or execution scope in
   * reactive environments).
   *
   * @return the current {@link TraceContext}, or {@code null} if no context is active
   */
  @Nullable TraceContext getCurrentContext();
}

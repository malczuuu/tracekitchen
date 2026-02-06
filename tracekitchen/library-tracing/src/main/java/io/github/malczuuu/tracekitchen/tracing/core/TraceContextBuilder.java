package io.github.malczuuu.tracekitchen.tracing.core;

/**
 * Builder for {@link TraceContext}.
 *
 * <p>Use this builder to create a {@link TraceContext} either from scratch or based on incoming
 * values (e.g., HTTP headers or messaging metadata).
 *
 * <p>Instances of this builder are immutable. Each {@code withX} method returns a new builder with
 * the updated value.
 *
 * <pre>{@code
 * TraceContext ctx = tracer.extractedContext()
 *     .withTraceId(headerTraceId)
 *     .withSpanId(headerSpanId)
 *     .withParentSpanId(parentSpanId)
 *     .build();
 * }</pre>
 */
public interface TraceContextBuilder {

  /**
   * Sets the trace ID for the context being built.
   *
   * @param traceId non-null trace ID
   * @return a new {@link TraceContextBuilder} with the trace ID set
   */
  TraceContextBuilder withTraceId(String traceId);

  /**
   * Sets the span ID for the context being built.
   *
   * @param spanId non-null span ID
   * @return a new {@link TraceContextBuilder} with the span ID set
   */
  TraceContextBuilder withSpanId(String spanId);

  /**
   * Sets the parent span ID for the context being built.
   *
   * @param parentSpanId parent span ID, or {@code null} if this is a root span
   * @return a new {@link TraceContextBuilder} with the parent span ID set
   */
  TraceContextBuilder withParentSpanId(String parentSpanId);

  TraceContext build();
}

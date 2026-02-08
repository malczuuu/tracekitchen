package io.github.malczuuu.tracekitchen;

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
 * TraceContext ctx = tracer.contextBuilder()
 *     .withTraceId(headerTraceId)
 *     .withSpanId(headerSpanId)
 *     .withParentSpanId(parentSpanId)
 *     .build();
 * }</pre>
 */
public interface TraceContextBuilder {

  /**
   * Sets the name for the context being built.
   *
   * @param name non-null name
   * @return a new {@link TraceContextBuilder} with the name set
   */
  TraceContextBuilder withName(String name);

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

  /**
   * Indicates whether this builder contains a complete trace context that can be used to continue
   * an existing trace.
   *
   * <p>This method returns {@code true} only if both {@code traceId} and {@code spanId} are present
   * and non-blank.
   *
   * <p>It does not validate identifier formats and does not indicate whether {@link #build()} will
   * generate a new root trace.
   *
   * @return {@code true} if a complete context is present; {@code false} otherwise
   */
  boolean isComplete();

  /**
   * Builds a {@link TraceContext}.
   *
   * <p>If this builder contains a complete trace context (both {@code traceId} and {@code spanId}),
   * the returned context represents a continuation of an existing trace.
   *
   * <p>If no complete context is present, this method creates a new root trace with randomly
   * generated identifiers.
   *
   * <p>Partial contexts (e.g. only {@code traceId} or only {@code spanId}) are ignored and treated
   * as absence of an existing trace.
   *
   * @return a new {@link TraceContext}, never {@code null}
   */
  TraceContext build();
}

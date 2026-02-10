package io.github.malczuuu.tracekit;

/**
 * Builder for {@link Span}.
 *
 * <p>Use this builder to create a {@link Span} either from scratch or based on incoming values
 * (e.g., HTTP headers or messaging metadata).
 *
 * <p>Instances of this builder are immutable. Each {@code withX} method returns a new builder with
 * the updated value.
 */
public interface SpanBuilder {

  /**
   * Sets the name for the span being built.
   *
   * @param name non-null name
   * @return a new {@link SpanBuilder} with the name set
   */
  SpanBuilder withName(String name);

  /**
   * Sets the trace ID for the span being built.
   *
   * @param traceId non-null trace ID
   * @return a new {@link SpanBuilder} with the trace ID set
   */
  SpanBuilder withTraceId(String traceId);

  /**
   * Sets the span ID for the span being built.
   *
   * @param spanId non-null span ID
   * @return a new {@link SpanBuilder} with the span ID set
   */
  SpanBuilder withSpanId(String spanId);

  /**
   * Sets the parent span ID for the span being built.
   *
   * @param parentSpanId parent span ID, or {@code null} if this is a root span
   * @return a new {@link SpanBuilder} with the parent span ID set
   */
  SpanBuilder withParentSpanId(String parentSpanId);

  /**
   * Sets the sampled flag for the span being built.
   *
   * @param sampled whether the span should be sampled
   * @return a new {@link SpanBuilder} with the sampled flag set
   */
  SpanBuilder withSampled(boolean sampled);

  /**
   * Indicates whether this builder contains a complete trace span that can be used to continue an
   * existing trace.
   *
   * <p>This method returns {@code true} only if both {@code traceId} and {@code spanId} are present
   * and non-blank.
   *
   * <p>It does not validate identifier formats and does not indicate whether {@link #build()} will
   * generate a new root trace.
   *
   * @return {@code true} if a complete span is present; {@code false} otherwise
   */
  boolean isComplete();

  /**
   * Builds a {@link Span}.
   *
   * <p>If this builder contains a complete trace span (both {@code traceId} and {@code spanId}),
   * the returned span represents a continuation of an existing trace.
   *
   * <p>If no complete span is present, this method creates a new root trace with randomly generated
   * identifiers.
   *
   * <p>Partial spans (e.g. only {@code traceId} or only {@code spanId}) are ignored and treated as
   * absence of an existing trace.
   *
   * @return a new {@link Span}, never {@code null}
   */
  Span build();
}

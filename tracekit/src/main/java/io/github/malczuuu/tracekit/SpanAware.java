package io.github.malczuuu.tracekit;

import org.jspecify.annotations.Nullable;

/**
 * Marks an object as carrying an associated {@link Span}.
 *
 * <p>This interface is typically implemented by components that are aware of the current tracing
 * spans, such as scopes, requests, messages, or execution wrappers.
 *
 * <p>The returned {@link Span} represents the span that is active or relevant for this object and
 * may be {@code null} if no span is associated.
 */
public interface SpanAware {

  /**
   * Returns the associated span.
   *
   * @return the associated {@link Span}, or {@code null} if none
   */
  @Nullable Span getSpan();
}

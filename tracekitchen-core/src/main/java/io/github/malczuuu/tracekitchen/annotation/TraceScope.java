package io.github.malczuuu.tracekitchen.annotation;

/**
 * Defines the propagation behavior of a {@code Span} when a {@link Traceable} method is invoked.
 *
 * <p>Analogous to transaction propagation in {@code @Transactional}:
 *
 * <ul>
 *   <li>{@link #REQUIRED} – Use the existing span if one exists, otherwise create a new root span.
 *       Nested calls create child spans.
 *   <li>{@link #REQUIRES_NEW} – Always create a new root span, independent of any current span.
 * </ul>
 *
 * @see io.github.malczuuu.tracekitchen.Span
 */
public enum TraceScope {

  /**
   * Use the current trace span if present; otherwise, create a new one. Nested calls create child
   * spans.
   */
  REQUIRED,

  /** Always create a new root trace span, ignoring any current one. */
  REQUIRES_NEW
}

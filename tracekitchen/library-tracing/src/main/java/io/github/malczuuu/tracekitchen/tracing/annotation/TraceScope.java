package io.github.malczuuu.tracekitchen.tracing.annotation;

/**
 * Defines the propagation behavior of a {@code TraceContext} when a {@link Traceable} method is
 * invoked.
 *
 * <p>Analogous to transaction propagation in {@code @Transactional}:
 *
 * <ul>
 *   <li>{@link #REQUIRED} – Use the existing context if one exists, otherwise create a new root
 *       context. Nested calls create child spans.
 *   <li>{@link #REQUIRES_NEW} – Always create a new root context, independent of any current
 *       context.
 * </ul>
 *
 * @see io.github.malczuuu.tracekitchen.tracing.core.TraceContext
 * @deprecated Marked as {@code @Deprecated} tue to its experimental nature. May change in the
 *     future, so use with caution.
 */
@Deprecated
public enum TraceScope {

  /**
   * Use the current trace context if present; otherwise, create a new one. Nested calls create
   * child spans.
   */
  REQUIRED,

  /** Always create a new root trace context, ignoring any current context. */
  REQUIRES_NEW
}

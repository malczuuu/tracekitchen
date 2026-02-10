package io.github.malczuuu.tracekitchen;

/**
 * Represents an opened span bound to the current execution scope.
 *
 * <p>An {@link OpenSpan} is a lifecycle-managed handle, typically used with {@code
 * try-with-resources}, that makes a {@link Span} active for the duration of the scope and restores
 * the previous span on close.
 */
public interface OpenSpan extends AutoCloseable, SpanAware {

  /**
   * Returns the active tracing span associated with this scope.
   *
   * @return the non-null active {@link Span}
   */
  @Override
  Span getSpan();

  /** Closes this span and restores the previously active one. */
  @Override
  void close();
}

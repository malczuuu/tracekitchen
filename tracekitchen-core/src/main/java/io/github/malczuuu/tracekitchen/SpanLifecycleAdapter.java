package io.github.malczuuu.tracekitchen;

import org.jspecify.annotations.Nullable;

/**
 * Adapter interface for receiving lifecycle events of a {@link Span}.
 *
 * <p>Implementations can use this to perform actions when a {@link Span} is opened or closed.
 */
public interface SpanLifecycleAdapter {

  /**
   * Called after a {@link Span} has been opened.
   *
   * @param span the {@link Span} that was opened
   * @param previousSpan the {@link Span} that was suspended due to opening {@code span}
   */
  default void afterOpened(Span span, @Nullable Span previousSpan) {}

  /**
   * Called after a {@link Span} has been closed.
   *
   * @param span the {@link Span} that was closed
   * @param currentSpan the {@link Span} that was resumed after closing {@code span}
   */
  default void afterClosed(Span span, @Nullable Span currentSpan) {}
}

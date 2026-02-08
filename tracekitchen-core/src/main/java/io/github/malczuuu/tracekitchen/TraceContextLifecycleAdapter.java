package io.github.malczuuu.tracekitchen;

import org.jspecify.annotations.Nullable;

/**
 * Adapter interface for receiving lifecycle events of a context.
 *
 * <p>Implementations can use this to perform actions when a context is opened or closed.
 */
public interface TraceContextLifecycleAdapter {

  /**
   * Called after a context has been opened.
   *
   * @param context the {@link TraceContext} that was opened
   * @param previousContext the {@link TraceContext} that was suspended due to opening {@code
   *     context}
   */
  default void afterOpened(TraceContext context, @Nullable TraceContext previousContext) {}

  /**
   * Called after a context has been closed.
   *
   * @param context the {@link TraceContext} that was closed
   * @param currentContext the {@link TraceContext} that was resumed after closing {@code context}
   */
  default void afterClosed(TraceContext context, @Nullable TraceContext currentContext) {}
}

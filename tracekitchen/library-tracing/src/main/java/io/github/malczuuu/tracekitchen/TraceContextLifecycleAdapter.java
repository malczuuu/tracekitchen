package io.github.malczuuu.tracekitchen;

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
   */
  default void afterOpened(TraceContext context) {}

  /**
   * Called after a context has been closed.
   *
   * @param context the {@link TraceContext} that was closed
   */
  default void afterClosed(TraceContext context) {}
}

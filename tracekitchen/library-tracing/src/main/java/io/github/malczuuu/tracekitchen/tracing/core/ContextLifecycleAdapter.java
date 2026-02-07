package io.github.malczuuu.tracekitchen.tracing.core;

/**
 * Adapter interface for receiving lifecycle events of a context.
 *
 * <p>Implementations can use this to perform actions when a context is opened or closed.
 */
public interface ContextLifecycleAdapter {

  /**
   * Called when a context has been opened.
   *
   * @param context the {@link TraceContext} that was opened
   */
  void onContextOpened(TraceContext context);

  /**
   * Called when a context has been closed.
   *
   * @param context the {@link TraceContext} that was closed
   */
  void onContextClosed(TraceContext context);
}

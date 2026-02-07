package io.github.malczuuu.tracekitchen;

/**
 * Adapter for integrating a {@link TraceContext} with a logging system.
 *
 * <p>Implementations of this interface are responsible for pushing the trace context ({@code
 * traceId}, {@code spanId}, {@code parentSpanId}, etc.) into the logging framework and removing it
 * when the scope ends.
 */
public interface LoggingContextAdapter {

  /**
   * Pushes the given {@link TraceContext} into the logging system.
   *
   * @param context the active trace context
   */
  void push(TraceContext context);

  /** Removes the trace context from the logging system. */
  void drop();
}

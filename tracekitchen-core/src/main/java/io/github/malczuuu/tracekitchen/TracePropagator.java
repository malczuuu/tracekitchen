package io.github.malczuuu.tracekitchen;

/**
 * Functional interface for propagating a {@link TraceContext} to an external system.
 *
 * <p>Implementations of this interface are responsible for sending trace identifiers ({@code
 * traceId}, {@code spanId}, {@code parentSpanId}, etc.) to outgoing requests, messages, or other
 * boundaries to continue a distributed trace.
 *
 * <p>Typical use cases include:
 *
 * <ul>
 *   <li>Adding trace headers to HTTP requests
 *   <li>Injecting trace information into messaging systems like Kafka
 *   <li>Custom instrumentation of RPC or async calls
 * </ul>
 */
@FunctionalInterface
public interface TracePropagator {

  /**
   * Propagates the given {@link TraceContext} to an external system.
   *
   * @param context the {@link TraceContext} to propagate
   */
  void propagate(TraceContext context);
}

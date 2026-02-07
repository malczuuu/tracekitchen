package io.github.malczuuu.tracekitchen.tracing.core;

/**
 * Factory for generating trace and span identifiers.
 *
 * <p>Provides methods to create unique identifiers for distributed tracing. Typically used by
 * {@link Tracer} when creating new {@link TraceContext} instances.
 *
 * <p>The default implementation generates 32-character hexadecimal strings for both trace IDs and
 * span IDs.
 */
public interface TraceFactory {

  /**
   * Generates a new unique trace ID.
   *
   * @return a non-null, trace ID string
   */
  String makeTraceId();

  /**
   * Generates a new unique span ID.
   *
   * @return a non-null, span ID string
   */
  String makeSpanId();
}

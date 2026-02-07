package io.github.malczuuu.tracekitchen;

/**
 * Functional interface for extracting a {@link TraceContext} from an external source.
 *
 * <p>Implementations are responsible for reading trace identifiers ({@code traceId}, {@code
 * spanId}, {@code parentSpanId}, etc.) from incoming requests, messages, or other execution
 * boundaries and returning a corresponding {@link TraceContext}.
 *
 * <p>Typical use cases include:
 *
 * <ul>
 *   <li>Extracting trace headers from HTTP requests
 *   <li>Extracting trace information from Kafka, RabbitMQ, or other messages
 *   <li>Custom instrumentation for RPC or async calls
 * </ul>
 *
 * @param <T> the type of the source object from which to extract the context
 */
@FunctionalInterface
public interface TraceExtractor<T> {

  /**
   * Extracts a {@link TraceContext} from the given source object.
   *
   * @param origin the source object (e.g., HTTP request, message) from which to extract trace info
   * @return the extracted {@link TraceContext}
   */
  TraceContext extract(T origin);
}

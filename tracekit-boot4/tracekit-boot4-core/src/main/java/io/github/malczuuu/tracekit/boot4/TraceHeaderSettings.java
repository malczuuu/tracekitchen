package io.github.malczuuu.tracekit.boot4;

import java.util.List;

/**
 * Interface defining the HTTP header names used for propagating tracing information across service
 * boundaries.
 *
 * <p>Implementations provide the names of headers that carry trace identifiers for distributed
 * tracing, including the current trace ID, the current span ID, and the parent span ID.
 *
 * <p>This allows tracing libraries to extract and inject these identifiers consistently when making
 * or receiving HTTP calls.
 */
public interface TraceHeaderSettings {

  /**
   * Returns the list of header names that should be used to propagate the trace ID.
   *
   * @return a list of HTTP header names for the trace ID
   */
  List<String> getTraceIdHeaderNames();

  /**
   * Returns the list of header names that should be used to propagate the current span ID.
   *
   * @return a list of HTTP header names for the span ID
   */
  List<String> getSpanIdHeaderNames();

  /**
   * Returns the list of header names that should be used to propagate the parent span ID.
   *
   * @return a list of HTTP header names for the parent span ID
   */
  List<String> getParentIdHeaderNames();
}

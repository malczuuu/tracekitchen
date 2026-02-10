package io.github.malczuuu.tracekitchen;

import org.jspecify.annotations.Nullable;

/**
 * Represents a trace context, which is a set of data that describes the current trace and span. It
 * typically includes a trace ID, a span ID, and optionally a parent span ID. The trace context is
 * used to correlate spans across different components and services in a distributed system.
 */
public interface Trace {

  /**
   * Returns the trace ID, which is a unique identifier for the entire trace. All spans that belong
   * to the same trace share the same trace ID.
   *
   * @return a non-null trace ID string
   */
  String getTraceId();

  /**
   * Returns the span ID, which is a unique identifier for the current span. Each span has its own
   * span ID, and it is used to identify the specific operation or unit of work being traced.
   *
   * @return a non-null span ID string
   */
  String getSpanId();

  /**
   * Returns the parent span ID, which is the span ID of the parent span that directly caused this
   * span to be created. If this span is a root span (i.e., it has no parent), then the parent span
   * ID may be null.
   *
   * @return the parent span ID string, or null if this is a root span
   */
  @Nullable String getParentSpanId();

  /**
   * Indicates whether this trace is sampled, meaning that it should be recorded and exported for
   * analysis. Sampling is a technique used to reduce the overhead of tracing by only recording a
   * subset of all traces. If this method returns true, it indicates that the trace is selected for
   * sampling and should be recorded; if it returns false, the trace is not sampled and may be
   * ignored by tracing systems.
   *
   * @return true if this trace is sampled, false otherwise
   */
  boolean isSampled();

  /**
   * Determines if this trace is the parent of the given trace. A trace is considered a parent of
   * another trace if they share the same trace ID and the span ID of this trace matches the parent
   * span ID of the other trace. This relationship indicates that the other trace was created as a
   * child span of this trace.
   *
   * @param trace the trace to check against; may be null
   * @return true if this trace is the parent of the given trace, false otherwise
   */
  boolean isParentOf(@Nullable Trace trace);

  /**
   * Determines if this trace is a child of the given trace. A trace is considered a child of
   * another trace if they share the same trace ID and the parent span ID of this trace matches the
   * span ID of the other trace. This relationship indicates that this trace was created as a child
   * span of the other trace.
   *
   * @param trace the trace to check against; may be null
   * @return true if this trace is a child of the given trace, false otherwise
   */
  boolean isChildOf(@Nullable Trace trace);

  /**
   * Creates a child trace of this trace. The child trace inherits the trace ID of this trace, and
   * sets the span ID of this trace as its parent span ID. The child trace has a new, unique span
   * ID. This method is used to create a new span that represents a unit of work that is a child of
   * the current span.
   *
   * @param spanId the span ID for the child trace; must be non-null and unique
   * @return a new Trace representing the child trace
   */
  Trace spawnChild(String spanId);
}

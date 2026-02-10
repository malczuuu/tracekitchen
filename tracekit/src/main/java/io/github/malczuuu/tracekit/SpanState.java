package io.github.malczuuu.tracekit;

/** Represents the lifecycle state of a {@link Span}. */
public enum SpanState {

  /** The span has been created but not yet opened. */
  NEW,

  /** The span has been opened and is currently active. */
  OPEN,

  /** The span has been closed and is no longer active. */
  CLOSED
}

package io.github.malczuuu.tracekitchen;

/** Represents the lifecycle state of a context. */
public enum ContextState {

  /** The context has been created but not yet opened. */
  NEW,

  /** The context has been opened and is currently active. */
  OPEN,

  /** The context has been closed and is no longer active. */
  CLOSED
}

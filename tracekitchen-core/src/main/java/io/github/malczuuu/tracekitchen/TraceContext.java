package io.github.malczuuu.tracekitchen;

/**
 * Represents an openable tracing context, to be opened within {@code try-with-resources} blocks to
 * manage the active context scope.
 *
 * <pre>{@code
 * TraceContext content = tracer.newRootContext();
 *
 * try (OpenContext open = parent.open()) {
 *   log.info("inside content scope");
 * }
 * }</pre>
 */
public interface TraceContext extends TraceContextSnapshot {

  /**
   * Opens the given context for the current execution scope.
   *
   * <p>Returns an {@link OpenContext} that should be used in a {@code try-with-resources} block to
   * ensure the previous context is restored on close.
   *
   * @return an {@link OpenContext} representing the active scope
   */
  OpenContext open();
}

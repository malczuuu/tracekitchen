package io.github.malczuuu.tracekitchen;

/**
 * Represents an opened tracing context bound to the current execution scope.
 *
 * <p>An {@link OpenContext} is a lifecycle-managed handle, typically used with {@code
 * try-with-resources}, that makes a {@link TraceContext} active for the duration of the scope and
 * restores the previous context on close.
 *
 * <pre>{@code
 * TraceContext parent = tracer.newRootContext();
 *
 * try (OpenContext parentScope = tracer.open(parent)) {
 *   log.info("inside parent span");
 *
 *   TraceContext child = parent.makeChild();
 *   try (OpenContext childScope = tracer.open(child)) {
 *     log.info("inside child span");
 *   }
 *
 *   log.info("back to parent span");
 * }
 * }</pre>
 */
public interface OpenContext extends AutoCloseable, TraceContextAware {

  /**
   * Returns the active tracing context associated with this scope.
   *
   * @return the non-null active {@link TraceContext}
   */
  @Override
  TraceContext getContext();

  /** Closes this context scope and restores the previously active context. */
  @Override
  void close();
}

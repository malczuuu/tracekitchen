package io.github.malczuuu.tracekitchen;

import org.jspecify.annotations.Nullable;

/**
 * Marks an object as carrying an associated {@link TraceContext}.
 *
 * <p>This interface is typically implemented by components that are aware of the current tracing
 * context, such as scopes, requests, messages, or execution wrappers.
 *
 * <p>The returned {@link TraceContext} represents the context that is active or relevant for this
 * object and may be {@code null} if no context is associated.
 */
public interface TraceContextAware {

  /**
   * Returns the associated tracing context.
   *
   * @return the associated {@link TraceContext}, or {@code null} if none
   */
  @Nullable TraceContext getContext();
}

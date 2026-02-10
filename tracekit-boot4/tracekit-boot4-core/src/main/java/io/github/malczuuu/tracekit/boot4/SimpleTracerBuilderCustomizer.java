package io.github.malczuuu.tracekit.boot4;

import io.github.malczuuu.tracekit.simple.SimpleTracerBuilder;

/**
 * Functional interface for customizing a {@link SimpleTracerBuilder}.
 *
 * <p>This interface allows modification of the builder before it is used to create a {@code
 * Tracer}. Implementations can adjust settings, add adapters, or change default behavior.
 *
 * <p>Being a functional interface, it can be used as a lambda or method reference.
 */
@FunctionalInterface
public interface SimpleTracerBuilderCustomizer {

  /**
   * Customize the given {@link SimpleTracerBuilder}.
   *
   * @param builder the builder to customize
   * @return the modified builder
   */
  SimpleTracerBuilder customize(SimpleTracerBuilder builder);
}

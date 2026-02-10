package io.github.malczuuu.tracekit.boot4;

/**
 * Functional interface alias for {@link SimpleTracerBuilderCustomizer} intended for use with
 * Spring's {@code ConditionalOnMissingBean}.
 *
 * <p>Marking a bean with this type allows conditional registration of a {@link
 * SimpleTracerBuilderCustomizer} only if no other bean of the same type is already present in the
 * Spring context.
 *
 * <p>Being a functional interface, it can be implemented using a lambda or method reference.
 *
 * @see org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
 */
@FunctionalInterface
public interface LoggingContextSimpleTracerBuilderCustomizer
    extends SimpleTracerBuilderCustomizer {}

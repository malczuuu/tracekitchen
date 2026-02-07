package io.github.malczuuu.tracekitchen.tracing.webmvc;

import io.github.malczuuu.tracekitchen.tracing.core.SimpleTracerBuilder;

@FunctionalInterface
public interface SimpleTracerBuilderCustomizer {

  SimpleTracerBuilder customize(SimpleTracerBuilder builder);
}

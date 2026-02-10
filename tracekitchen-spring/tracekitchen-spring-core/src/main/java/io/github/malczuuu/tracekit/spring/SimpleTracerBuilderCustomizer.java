package io.github.malczuuu.tracekit.spring;

import io.github.malczuuu.tracekit.simple.SimpleTracerBuilder;

@FunctionalInterface
public interface SimpleTracerBuilderCustomizer {

  SimpleTracerBuilder customize(SimpleTracerBuilder builder);
}

package io.github.malczuuu.tracekitchen.spring;

import io.github.malczuuu.tracekitchen.simple.SimpleTracerBuilder;

@FunctionalInterface
public interface SimpleTracerBuilderCustomizer {

  SimpleTracerBuilder customize(SimpleTracerBuilder builder);
}

package io.github.malczuuu.tracekit.boot4;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.malczuuu.tracekit.OpenSpan;
import io.github.malczuuu.tracekit.Span;
import io.github.malczuuu.tracekit.Tracer;
import io.github.malczuuu.tracekit.simple.SimpleTracer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TracingTaskDecoratorTest {

  private Tracer tracer;
  private TracingTaskDecorator decorator;

  private Span testSpan;

  @BeforeEach
  void setUp() {
    tracer = SimpleTracer.create();
    decorator = new TracingTaskDecorator(tracer);
    testSpan = null;
  }

  @Test
  void givenNoActiveSpan_whenDecorating_shouldReturnOriginalRunnable() {
    Runnable original = () -> testSpan = tracer.getCurrentSpan();

    Runnable decorated = decorator.decorate(original);

    assertThat(decorated).isSameAs(original);
    decorated.run();

    assertThat(testSpan).isNull();
  }

  @Test
  void givenActiveSpan_whenDecorating_shouldReturnDecoratedRunnable() {
    Span span = tracer.root("test-span");
    try (OpenSpan open = span.open()) {
      Runnable original = () -> testSpan = tracer.getCurrentSpan();

      Runnable decorated = decorator.decorate(original);

      assertThat(decorated).isNotSameAs(original);
      decorated.run();
      assertThat(testSpan).matches(it -> it.getTrace().isChildOf(span.getTrace()));
    }
  }
}

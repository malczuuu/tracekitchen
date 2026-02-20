/*
 * Copyright (c) 2026 Damian Malczewski
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.github.malczuuu.tracekit.boot4;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.malczuuu.tracekit.OpenSpan;
import io.github.malczuuu.tracekit.Span;
import io.github.malczuuu.tracekit.Tracer;
import io.github.malczuuu.tracekit.simple.SimpleTracer;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@NullUnmarked
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
    Runnable original = () -> testSpan = tracer.findCurrentSpan().orElse(null);

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

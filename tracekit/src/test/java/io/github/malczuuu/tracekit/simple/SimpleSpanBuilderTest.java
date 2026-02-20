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

package io.github.malczuuu.tracekit.simple;

import static org.assertj.core.api.Assertions.assertThat;

import com.mercateo.test.clock.TestClock;
import io.github.malczuuu.tracekit.Span;
import io.github.malczuuu.tracekit.SpanBuilder;
import io.github.malczuuu.tracekit.SpanLifecycleAdapter;
import io.github.malczuuu.tracekit.TraceFactory;
import java.time.Clock;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SimpleSpanBuilderTest {

  private Clock clock;
  private SpanLifecycleAdapter lifecycleAdapter;
  private TraceFactory traceFactory;

  @BeforeEach
  void beforeEach() {
    clock = TestClock.fixed(OffsetDateTime.parse("2025-09-22T12:33:17Z"));
    lifecycleAdapter = CompositeLifecycleAdapter.empty();
    traceFactory = SimpleTraceFactory.getInstance();
  }

  @Test
  void givenBuilder_whenFillingValues_shouldConstructNewObjectOnEachStep() {
    SpanBuilder builder = new SimpleSpanBuilder(clock, lifecycleAdapter, traceFactory);

    var afterStep1 = builder.withTraceId("traceId");
    var afterStep2 = afterStep1.withSpanId("spanId");
    var afterStep3 = afterStep2.withParentSpanId("parentSpanId");

    Span span = afterStep3.build();

    assertThat(builder).isNotSameAs(afterStep1);
    assertThat(afterStep1).isNotSameAs(afterStep2);
    assertThat(afterStep2).isNotSameAs(afterStep3);

    assertThat(span.getTrace().getTraceId()).isEqualTo("traceId");
    assertThat(span.getTrace().getSpanId()).isEqualTo("spanId");
    assertThat(span.getTrace().getParentSpanId()).isEqualTo("parentSpanId");
  }
}

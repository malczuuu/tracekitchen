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
import io.github.malczuuu.tracekit.SpanLifecycleAdapter;
import io.github.malczuuu.tracekit.Tracer;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.concurrent.atomic.AtomicInteger;
import org.assertj.core.api.Assertions;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SimpleTracerTest {

  private TestClock clock;

  private Tracer tracer;

  @BeforeEach
  void beforeEach() {
    clock = TestClock.fixed(OffsetDateTime.parse("2025-09-22T12:33:17Z"));
    tracer = new SimpleTracerBuilder().withClock(clock).build();
  }

  @Test
  void givenParentContext_whenOpeningChild_thenCurrentIsUpdatedAndRestored() {
    Span parent = tracer.root();

    Assertions.assertThat(ThreadLocalSpanHolder.current()).isNull();

    try (var op = parent.open()) {
      assertThat(ThreadLocalSpanHolder.current()).isSameAs(parent);

      Span child = parent.spawnChild();
      try (var oc = child.open()) {
        assertThat(ThreadLocalSpanHolder.current()).isSameAs(child);
      }

      assertThat(ThreadLocalSpanHolder.current()).isSameAs(parent);
    }

    assertThat(ThreadLocalSpanHolder.current()).isNull();
  }

  @Test
  void givenParentContext_whenOpeningAndClosing_tenContextLifecycleIsTracked() {
    AtomicInteger onOpenedHits = new AtomicInteger(0);
    AtomicInteger onClosedHits = new AtomicInteger(0);

    Tracer tracer =
        new SimpleTracerBuilder()
            .addLifecycleAdapter(
                new SpanLifecycleAdapter() {
                  @Override
                  public void afterOpened(@NonNull Span span, @Nullable Span previousSpan) {
                    onOpenedHits.incrementAndGet();
                  }

                  @Override
                  public void afterClosed(@NonNull Span span, @Nullable Span currentSpan) {
                    onClosedHits.incrementAndGet();
                  }
                })
            .withClock(clock)
            .build();

    Span parent = tracer.root();

    try (var op = parent.open()) {
      assertThat(onOpenedHits).hasValue(1);
      assertThat(onClosedHits).hasValue(0);

      Span child = parent.spawnChild();
      try (var oc = child.open()) {
        assertThat(onOpenedHits).hasValue(2);
        assertThat(onClosedHits).hasValue(0);
      }
      assertThat(onOpenedHits).hasValue(2);
      assertThat(onClosedHits).hasValue(1);
    }
    assertThat(onOpenedHits).hasValue(2);
    assertThat(onClosedHits).hasValue(2);
  }

  @Test
  void givenMultipleLifecycleAdapters_whenOpeningAndClosing_thenAllAdaptersAreInvoked() {
    AtomicInteger adapter1Opened = new AtomicInteger();
    AtomicInteger adapter1Closed = new AtomicInteger();

    AtomicInteger adapter2Opened = new AtomicInteger();
    AtomicInteger adapter2Closed = new AtomicInteger();

    SpanLifecycleAdapter adapter1 =
        new SpanLifecycleAdapter() {
          @Override
          public void afterOpened(@NonNull Span span, @Nullable Span previousSpan) {
            adapter1Opened.incrementAndGet();
          }

          @Override
          public void afterClosed(@NonNull Span span, @Nullable Span currentSpan) {
            adapter1Closed.incrementAndGet();
          }
        };

    SpanLifecycleAdapter adapter2 =
        new SpanLifecycleAdapter() {
          @Override
          public void afterOpened(@NonNull Span span, @Nullable Span previousSpan) {
            adapter2Opened.incrementAndGet();
          }

          @Override
          public void afterClosed(@NonNull Span span, @Nullable Span currentSpan) {
            adapter2Closed.incrementAndGet();
          }
        };

    Tracer tracer =
        new SimpleTracerBuilder()
            .addLifecycleAdapter(adapter1)
            .addLifecycleAdapter(adapter2)
            .withClock(clock)
            .build();

    Span parent = tracer.root();

    try (var p = parent.open()) {
      assertThat(adapter1Opened).hasValue(1);
      assertThat(adapter2Opened).hasValue(1);
      assertThat(adapter1Closed).hasValue(0);
      assertThat(adapter2Closed).hasValue(0);

      Span child = parent.spawnChild();
      try (var c = child.open()) {
        assertThat(adapter1Opened).hasValue(2);
        assertThat(adapter2Opened).hasValue(2);
        assertThat(adapter1Closed).hasValue(0);
        assertThat(adapter2Closed).hasValue(0);
      }

      assertThat(adapter1Closed).hasValue(1);
      assertThat(adapter2Closed).hasValue(1);
    }

    assertThat(adapter1Closed).hasValue(2);
    assertThat(adapter2Closed).hasValue(2);
  }

  @Test
  void givenNestedContexts_whenRetrievingCurrentContext_shouldUseCorrectOne() {
    Span parent = tracer.root();

    assertThat(tracer.findCurrentSpan()).isEmpty();

    try (var op = parent.open()) {
      Span parentSpan = tracer.getCurrentSpan();
      assertThat(parentSpan).isNotNull().extracting(Span::getTrace).isEqualTo(parent.getTrace());

      Span child = parent.spawnChild();
      try (var oc = child.open()) {
        Span childSpan = tracer.getCurrentSpan();
        assertThat(childSpan).isNotNull();
        assertThat(childSpan).isNotNull().extracting(Span::getTrace).isEqualTo(child.getTrace());
      }

      parentSpan = tracer.getCurrentSpan();
      assertThat(parentSpan).isNotNull();
      assertThat(parentSpan).isNotNull().extracting(Span::getTrace).isEqualTo(parent.getTrace());
    }

    assertThat(tracer.findCurrentSpan()).isEmpty();
  }

  @Test
  void givenOpenContext_shouldBeAwareOfCurrent() {
    Span parent = tracer.root();

    try (var op = parent.open()) {
      assertThat(op.getSpan().getTrace()).isEqualTo(parent.getTrace());

      Span child = parent.spawnChild();
      try (var oc = child.open()) {
        assertThat(oc.getSpan().getTrace()).isSameAs(child.getTrace());
      }

      assertThat(op.getSpan().getTrace()).isEqualTo(parent.getTrace());
    }
  }

  @Test
  void givenParentAndChildContext_whenOpeningAndClosingContext_shouldTrackDuration() {
    Span parent = tracer.root();
    Span child;
    try (var op = parent.open()) {
      child = parent.spawnChild();

      clock.fastForward(Duration.ofSeconds(3));
      try (var oc = child.open()) {
        clock.fastForward(Duration.ofSeconds(1));
      }
      clock.fastForward(Duration.ofSeconds(2));
    }

    assertThat(parent.getDuration()).isEqualTo(Duration.ofSeconds(6));
    assertThat(child.getDuration()).isEqualTo(Duration.ofSeconds(1));
  }
}

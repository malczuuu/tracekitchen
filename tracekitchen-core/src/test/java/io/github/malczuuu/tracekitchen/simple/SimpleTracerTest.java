package io.github.malczuuu.tracekitchen.simple;

import static org.assertj.core.api.Assertions.assertThat;

import com.mercateo.test.clock.TestClock;
import io.github.malczuuu.tracekitchen.Span;
import io.github.malczuuu.tracekitchen.SpanLifecycleAdapter;
import io.github.malczuuu.tracekitchen.Tracer;
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

    assertThat(tracer.getCurrentSpan()).isNull();

    try (var op = parent.open()) {
      assertThat(tracer.getCurrentSpan().getTrace()).isEqualTo(parent.getTrace());

      Span child = parent.spawnChild();
      try (var oc = child.open()) {
        assertThat(tracer.getCurrentSpan().getTrace()).isEqualTo(child.getTrace());
      }

      assertThat(tracer.getCurrentSpan().getTrace()).isEqualTo(parent.getTrace());
    }

    assertThat(tracer.getCurrentSpan()).isNull();
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

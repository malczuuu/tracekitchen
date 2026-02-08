package io.github.malczuuu.tracekitchen;

import static org.assertj.core.api.Assertions.assertThat;

import com.mercateo.test.clock.TestClock;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.concurrent.atomic.AtomicInteger;
import org.assertj.core.api.Assertions;
import org.jspecify.annotations.NonNull;
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
    TraceContext parent = tracer.newRootContext();

    Assertions.assertThat(ContextThreadLocalHolder.current()).isNull();

    try (var op = tracer.open(parent)) {
      assertThat(ContextThreadLocalHolder.current()).isSameAs(parent);

      TraceContext child = parent.makeChild();
      try (var oc = tracer.open(child)) {
        assertThat(ContextThreadLocalHolder.current()).isSameAs(child);
      }

      assertThat(ContextThreadLocalHolder.current()).isSameAs(parent);
    }

    assertThat(ContextThreadLocalHolder.current()).isNull();
  }

  @Test
  void givenParentContext_whenOpeningAndClosing_tenContextLifecycleIsTracked() {
    AtomicInteger onOpenedHits = new AtomicInteger(0);
    AtomicInteger onClosedHits = new AtomicInteger(0);

    Tracer tracer =
        new SimpleTracerBuilder()
            .addLifecycleAdapter(
                new TraceContextLifecycleAdapter() {
                  @Override
                  public void afterOpened(@NonNull TraceContext context) {
                    onOpenedHits.incrementAndGet();
                  }

                  @Override
                  public void afterClosed(@NonNull TraceContext context) {
                    onClosedHits.incrementAndGet();
                  }
                })
            .withClock(clock)
            .build();

    TraceContext parent = tracer.newRootContext();

    try (var op = tracer.open(parent)) {
      assertThat(onOpenedHits).hasValue(1);
      assertThat(onClosedHits).hasValue(0);

      TraceContext child = parent.makeChild();
      try (var oc = tracer.open(child)) {
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

    TraceContextLifecycleAdapter adapter1 =
        new TraceContextLifecycleAdapter() {
          @Override
          public void afterOpened(@NonNull TraceContext context) {
            adapter1Opened.incrementAndGet();
          }

          @Override
          public void afterClosed(@NonNull TraceContext context) {
            adapter1Closed.incrementAndGet();
          }
        };

    TraceContextLifecycleAdapter adapter2 =
        new TraceContextLifecycleAdapter() {
          @Override
          public void afterOpened(@NonNull TraceContext context) {
            adapter2Opened.incrementAndGet();
          }

          @Override
          public void afterClosed(@NonNull TraceContext context) {
            adapter2Closed.incrementAndGet();
          }
        };

    Tracer tracer =
        new SimpleTracerBuilder()
            .addLifecycleAdapter(adapter1)
            .addLifecycleAdapter(adapter2)
            .withClock(clock)
            .build();

    TraceContext parent = tracer.newRootContext();

    try (var p = tracer.open(parent)) {
      assertThat(adapter1Opened).hasValue(1);
      assertThat(adapter2Opened).hasValue(1);
      assertThat(adapter1Closed).hasValue(0);
      assertThat(adapter2Closed).hasValue(0);

      TraceContext child = parent.makeChild();
      try (var c = tracer.open(child)) {
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
    TraceContext parent = tracer.newRootContext();

    assertThat(tracer.getCurrentContext()).isNull();

    try (var op = tracer.open(parent)) {
      assertThat(tracer.getCurrentContext()).isSameAs(parent);

      TraceContext child = parent.makeChild();
      try (var oc = tracer.open(child)) {
        assertThat(tracer.getCurrentContext()).isSameAs(child);
      }

      assertThat(tracer.getCurrentContext()).isSameAs(parent);
    }

    assertThat(tracer.getCurrentContext()).isNull();
  }

  @Test
  void givenOpenContext_shouldBeAwareOfCurrent() {
    TraceContext parent = tracer.newRootContext();

    try (var op = tracer.open(parent)) {
      assertThat(op.getContext()).isSameAs(parent);

      TraceContext child = parent.makeChild();
      try (var oc = tracer.open(child)) {
        assertThat(oc.getContext()).isSameAs(child);
      }

      assertThat(op.getContext()).isSameAs(parent);
    }
  }

  @Test
  void givenParentAndChildContext_whenOpeningAndClosingContext_shouldTrackDuration() {
    TraceContext parent = tracer.newRootContext();
    TraceContext child;
    try (var op = tracer.open(parent)) {
      child = parent.makeChild();

      clock.fastForward(Duration.ofSeconds(3));
      try (var oc = tracer.open(child)) {
        clock.fastForward(Duration.ofSeconds(1));
      }
      clock.fastForward(Duration.ofSeconds(2));
    }

    assertThat(parent.getDuration()).isEqualTo(Duration.ofSeconds(6));
    assertThat(child.getDuration()).isEqualTo(Duration.ofSeconds(1));
  }
}

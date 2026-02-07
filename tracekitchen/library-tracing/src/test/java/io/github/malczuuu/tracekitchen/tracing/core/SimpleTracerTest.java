package io.github.malczuuu.tracekitchen.tracing.core;

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
    TraceContext parent = tracer.createContext();

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
  void givenParentContext_whenOpeningChild_thenContextIsAdapterIntoLogging() {
    AtomicInteger pushHits = new AtomicInteger(0);
    AtomicInteger dropHits = new AtomicInteger(0);

    Tracer tracer =
        new SimpleTracerBuilder()
            .withLoggingContextAdapter(
                new LoggingContextAdapter() {
                  @Override
                  public void push(@NonNull TraceContext context) {
                    pushHits.incrementAndGet();
                  }

                  @Override
                  public void drop() {
                    dropHits.incrementAndGet();
                  }
                })
            .withClock(clock)
            .build();

    TraceContext parent = tracer.createContext();

    try (var op = tracer.open(parent)) {
      assertThat(pushHits).hasValue(1);
      assertThat(dropHits).hasValue(0);

      TraceContext child = parent.makeChild();
      try (var oc = tracer.open(child)) {
        assertThat(pushHits).hasValue(2);
        assertThat(dropHits).hasValue(0);
      }
      assertThat(pushHits).hasValue(3);
      assertThat(dropHits).hasValue(0);
    }
    assertThat(pushHits).hasValue(3);
    assertThat(dropHits).hasValue(1);
  }

  @Test
  void givenParentContext_whenOpeningAndClosing_tenContextLifecycleIsTracked() {
    AtomicInteger onOpenedHits = new AtomicInteger(0);
    AtomicInteger onClosedHits = new AtomicInteger(0);

    Tracer tracer =
        new SimpleTracerBuilder()
            .withContextLifecycleAdapter(
                new ContextLifecycleAdapter() {
                  @Override
                  public void onContextOpened(@NonNull TraceContext context) {
                    onOpenedHits.incrementAndGet();
                  }

                  @Override
                  public void onContextClosed(@NonNull TraceContext context) {
                    onClosedHits.incrementAndGet();
                  }
                })
            .withClock(clock)
            .build();

    TraceContext parent = tracer.createContext();

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
  void givenNestedContexts_whenRetrievingCurrentContext_shouldUseCorrectOne() {
    TraceContext parent = tracer.createContext();

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
    TraceContext parent = tracer.createContext();

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
    TraceContext parent = tracer.createContext();
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

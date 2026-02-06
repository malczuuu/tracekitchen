package io.github.malczuuu.tracekitchen.tracing.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicInteger;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SimpleTracerTest {

  private Tracer tracer;

  @BeforeEach
  void beforeEach() {
    tracer = new SimpleTracer(TraceFactory.getInstance());
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
        new SimpleTracer(
            TraceFactory.getInstance(), new RecordingLoggingAdapter(pushHits, dropHits));

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
}

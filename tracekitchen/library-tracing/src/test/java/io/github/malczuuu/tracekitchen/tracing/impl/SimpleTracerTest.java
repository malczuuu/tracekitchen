package io.github.malczuuu.tracekitchen.tracing.impl;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.malczuuu.tracekitchen.tracing.api.Context;
import io.github.malczuuu.tracekitchen.tracing.api.Tracer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

class SimpleTracerTest {

  private Tracer tracer;

  @BeforeEach
  void beforeEach() {
    tracer = new SimpleTracer();
  }

  @Test
  void givenParentContext_whenOpeningChild_thenCurrentIsUpdatedAndRestored() {
    Context parent = tracer.createContext();

    Assertions.assertThat(ContextHolder.current()).isNull();

    try (var op = tracer.openContext(parent)) {
      assertThat(ContextHolder.current()).isSameAs(parent);

      Context child = parent.makeChild();
      try (var oc = tracer.openContext(child)) {
        assertThat(ContextHolder.current()).isSameAs(child);
      }

      assertThat(ContextHolder.current()).isSameAs(parent);
    }

    assertThat(ContextHolder.current()).isNull();
  }

  @Test
  void givenParentContext_whenOpeningChild_thenMDCIsUpdatedAndRestored() {
    Context parent = tracer.createContext();

    assertThat(MDC.get("traceId")).isNull();
    assertThat(MDC.get("spanId")).isNull();

    try (var op = tracer.openContext(parent)) {
      assertThat(MDC.get("traceId")).isEqualTo(parent.getTraceId());
      assertThat(MDC.get("spanId")).isEqualTo(parent.getSpanId());

      Context child = parent.makeChild();
      try (var oc = tracer.openContext(child)) {
        assertThat(MDC.get("traceId")).isEqualTo(child.getTraceId());
        assertThat(MDC.get("spanId")).isEqualTo(child.getSpanId());
      }

      assertThat(MDC.get("traceId")).isEqualTo(parent.getTraceId());
      assertThat(MDC.get("spanId")).isEqualTo(parent.getSpanId());
    }

    assertThat(MDC.get("traceId")).isNull();
    assertThat(MDC.get("spanId")).isNull();
  }
}

package io.github.malczuuu.tracekitchen.tracing.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TraceContextImplTest {

  @Test
  void givenContext_whenSpawningChild_shouldRetainParentSpanId() {
    TraceContext parent = new TraceContextImpl(TraceFactory.getInstance());

    TraceContext child = parent.makeChild();

    assertThat(parent.getParentSpanId()).isNull();
    assertThat(child.getTraceId()).isEqualTo(parent.getTraceId());
    assertThat(child.getSpanId()).isNotEqualTo(parent.getSpanId());
    assertThat(child.getParentSpanId()).isEqualTo(parent.getSpanId());
  }
}

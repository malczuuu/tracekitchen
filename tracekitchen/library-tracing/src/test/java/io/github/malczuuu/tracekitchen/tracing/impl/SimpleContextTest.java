package io.github.malczuuu.tracekitchen.tracing.impl;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.malczuuu.tracekitchen.tracing.api.Context;
import org.junit.jupiter.api.Test;

class SimpleContextTest {

  @Test
  void givenContext_whenSpawningChild_shouldRetainParentSpanId() {
    Context parent = new SimpleContext();

    Context child = parent.makeChild();

    assertThat(parent.getParentSpanId()).isNull();
    assertThat(child.getTraceId()).isEqualTo(parent.getTraceId());
    assertThat(child.getSpanId()).isNotEqualTo(parent.getSpanId());
    assertThat(child.getParentSpanId()).isEqualTo(parent.getSpanId());
  }
}

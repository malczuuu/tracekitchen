package io.github.malczuuu.tracekitchen.tracing.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TraceFactoryImplTest {

  private TraceFactory traceFactory;

  @BeforeEach
  void beforeEach() {
    traceFactory = SimpleTraceFactory.getInstance();
  }

  @Test
  void whenGeneratingTraceId_shouldBe32CharactersLong() {
    String id = traceFactory.makeTraceId();

    assertThat(id).hasSize(32);
  }

  @Test
  void whenGeneratingTraceId_doesNotContainDashes() {
    String id = traceFactory.makeTraceId();

    assertThat(id).doesNotContain("-");
  }

  @Test
  void whenGeneratingSpanId_shouldBe16CharactersLong() {
    String id = traceFactory.makeSpanId();

    assertThat(id).hasSize(16);
  }

  @Test
  void whenGeneratingSpanId_doesNotContainDashes() {
    String id = traceFactory.makeSpanId();

    assertThat(id).doesNotContain("-");
  }
}

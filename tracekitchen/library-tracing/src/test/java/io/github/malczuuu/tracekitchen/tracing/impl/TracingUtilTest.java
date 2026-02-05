package io.github.malczuuu.tracekitchen.tracing.impl;

import static io.github.malczuuu.tracekitchen.tracing.impl.TracingUtil.generateId;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TracingUtilTest {

  @Test
  void whenGeneratingId_shouldBe32CharactersLong() {
    String id = generateId();

    assertThat(id).hasSize(32);
  }

  @Test
  void whenGeneratingId_doesNotContainDashes() {
    String id = generateId();

    assertThat(id).doesNotContain("-");
  }
}

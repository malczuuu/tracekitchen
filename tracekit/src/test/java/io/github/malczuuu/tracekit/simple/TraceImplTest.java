package io.github.malczuuu.tracekit.simple;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.malczuuu.tracekit.Trace;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TraceImplTest {

  @Test
  void shouldInitializeWithBasicConstructor() {
    TraceImpl trace = new TraceImpl("trace-1", "span-1");

    assertThat(trace.getTraceId()).isEqualTo("trace-1");
    assertThat(trace.getSpanId()).isEqualTo("span-1");
    assertThat(trace.getParentSpanId()).isNull();
    assertThat(trace.isSampled()).isFalse();
  }

  @Test
  void shouldProduceCorrectToString() {
    Trace traceWithParent = new TraceImpl("t1", "s2", "s1", true);
    Trace traceWithoutParent = new TraceImpl("t1", "s1");

    assertThat(traceWithParent.toString())
        .contains("traceId='t1'", "spanId='s2'", "parentSpanId='s1'", "sampled=true");

    assertThat(traceWithoutParent.toString()).doesNotContain("parentSpanId");
  }

  @Nested
  class HierarchyTests {

    @Test
    void shouldSpawnChild() {
      Trace parent = new TraceImpl("t1", "s1", null, true);
      Trace child = parent.spawnChild("s2");

      assertThat(child.getTraceId()).isEqualTo("t1");
      assertThat(child.getSpanId()).isEqualTo("s2");
      assertThat(child.getParentSpanId()).isEqualTo("s1");
      assertThat(child.isSampled()).isTrue(); // Sampled state should be inherited
    }

    @Test
    void shouldIdentifyRelationships() {
      Trace parent = new TraceImpl("t1", "s1");
      Trace child = parent.spawnChild("s2");
      Trace stranger = new TraceImpl("t2", "s3");

      assertThat(parent.isParentOf(child)).isTrue();
      assertThat(child.isChildOf(parent)).isTrue();

      assertThat(parent.isParentOf(stranger)).isFalse();
      assertThat(child.isChildOf(stranger)).isFalse();
    }

    @Test
    void shouldHandleNullsInRelationships() {
      Trace trace = new TraceImpl("t1", "s1");

      assertThat(trace.isParentOf(null)).isFalse();
      assertThat(trace.isChildOf(null)).isFalse();
    }
  }

  @Nested
  class EqualityTests {

    @Test
    void shouldBeEqual() {
      Trace t1 = new TraceImpl("t", "s", "p", true);
      Trace t2 = new TraceImpl("t", "s", "p", true);

      assertThat(t1).isEqualTo(t2);
      assertThat(t1.hashCode()).isEqualTo(t2.hashCode());
    }

    @Test
    void shouldNotBeEqual() {
      Trace base = new TraceImpl("t", "s", "p", true);

      assertThat(base).isNotEqualTo(new TraceImpl("diff", "s", "p", true));
      assertThat(base).isNotEqualTo(new TraceImpl("t", "diff", "p", true));
      assertThat(base).isNotEqualTo(new TraceImpl("t", "s", "diff", true));
      assertThat(base).isNotEqualTo(new TraceImpl("t", "s", "p", false));
    }

    @Test
    void shouldHandleEdgeCasesInEquals() {
      Trace trace = new TraceImpl("t", "s");

      assertThat(trace.equals(null)).isFalse();
      assertThat(trace.equals("some string")).isFalse();
    }
  }
}

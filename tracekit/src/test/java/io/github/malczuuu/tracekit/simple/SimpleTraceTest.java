/*
 * Copyright (c) 2026 Damian Malczewski
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.github.malczuuu.tracekit.simple;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.malczuuu.tracekit.Trace;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class SimpleTraceTest {

  @Test
  void shouldInitializeWithBasicConstructor() {
    SimpleTrace trace = new SimpleTrace("trace-1", "span-1");

    assertThat(trace.getTraceId()).isEqualTo("trace-1");
    assertThat(trace.getSpanId()).isEqualTo("span-1");
    assertThat(trace.getParentSpanId()).isNull();
    assertThat(trace.isSampled()).isFalse();
  }

  @Test
  void shouldProduceCorrectToString() {
    Trace traceWithParent = new SimpleTrace("t1", "s2", "s1", true);
    Trace traceWithoutParent = new SimpleTrace("t1", "s1");

    assertThat(traceWithParent.toString())
        .contains("traceId='t1'", "spanId='s2'", "parentSpanId='s1'", "sampled=true");

    assertThat(traceWithoutParent.toString()).doesNotContain("parentSpanId");
  }

  @Nested
  class HierarchyTests {

    @Test
    void shouldSpawnChild() {
      Trace parent = new SimpleTrace("t1", "s1", null, true);
      Trace child = parent.spawnChild("s2");

      assertThat(child.getTraceId()).isEqualTo("t1");
      assertThat(child.getSpanId()).isEqualTo("s2");
      assertThat(child.getParentSpanId()).isEqualTo("s1");
      assertThat(child.isSampled()).isTrue(); // Sampled state should be inherited
    }

    @Test
    void shouldIdentifyRelationships() {
      Trace parent = new SimpleTrace("t1", "s1");
      Trace child = parent.spawnChild("s2");
      Trace stranger = new SimpleTrace("t2", "s3");

      assertThat(parent.isParentOf(child)).isTrue();
      assertThat(child.isChildOf(parent)).isTrue();

      assertThat(parent.isParentOf(stranger)).isFalse();
      assertThat(child.isChildOf(stranger)).isFalse();
    }

    @Test
    void shouldHandleNullsInRelationships() {
      Trace trace = new SimpleTrace("t1", "s1");

      assertThat(trace.isParentOf(null)).isFalse();
      assertThat(trace.isChildOf(null)).isFalse();
    }
  }

  @Nested
  class EqualityTests {

    @Test
    void shouldBeEqual() {
      Trace t1 = new SimpleTrace("t", "s", "p", true);
      Trace t2 = new SimpleTrace("t", "s", "p", true);

      assertThat(t1).isEqualTo(t2);
      assertThat(t1.hashCode()).isEqualTo(t2.hashCode());
    }

    @Test
    void shouldNotBeEqual() {
      Trace base = new SimpleTrace("t", "s", "p", true);

      assertThat(base).isNotEqualTo(new SimpleTrace("diff", "s", "p", true));
      assertThat(base).isNotEqualTo(new SimpleTrace("t", "diff", "p", true));
      assertThat(base).isNotEqualTo(new SimpleTrace("t", "s", "diff", true));
      assertThat(base).isNotEqualTo(new SimpleTrace("t", "s", "p", false));
    }

    @Test
    void shouldHandleEdgeCasesInEquals() {
      Trace trace = new SimpleTrace("t", "s");

      assertThat(trace.equals(null)).isFalse();
      assertThat(trace.equals("some string")).isFalse();
    }
  }
}

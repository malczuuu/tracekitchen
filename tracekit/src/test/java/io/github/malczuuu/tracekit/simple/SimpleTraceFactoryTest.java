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

import io.github.malczuuu.tracekit.TraceFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SimpleTraceFactoryTest {

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

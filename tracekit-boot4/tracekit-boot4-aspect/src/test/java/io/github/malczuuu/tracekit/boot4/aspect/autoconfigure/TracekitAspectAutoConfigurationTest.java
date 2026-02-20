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

package io.github.malczuuu.tracekit.boot4.aspect.autoconfigure;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.malczuuu.tracekit.boot4.aspect.ScheduledTracingAspect;
import io.github.malczuuu.tracekit.boot4.aspect.TraceableTracingAspect;
import io.github.malczuuu.tracekit.boot4.autoconfigure.TracekitAutoConfiguration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

class TracekitAspectAutoConfigurationTest {

  @ActiveProfiles(profiles = {"test"})
  @SpringBootTest(
      classes = {TracekitAutoConfiguration.class, TracekitAspectAutoConfiguration.class},
      properties = {"tracekit.aspect.enabled=true"})
  @Nested
  class WithTracekitAspectEnabledTrue {

    @Autowired(required = false)
    ScheduledTracingAspect scheduledTracingAspect;

    @Autowired(required = false)
    TraceableTracingAspect traceableTracingAspect;

    @Test
    void contextLoads() {
      assertThat(scheduledTracingAspect).isNotNull();
      assertThat(traceableTracingAspect).isNotNull();
    }
  }

  @ActiveProfiles(profiles = {"test"})
  @SpringBootTest(
      classes = {TracekitAutoConfiguration.class, TracekitAspectAutoConfiguration.class},
      properties = {"tracekit.aspect.enabled=false"})
  @Nested
  class WithTracekitAspectEnabledFalse {

    @Autowired(required = false)
    ScheduledTracingAspect scheduledTracingAspect;

    @Autowired(required = false)
    TraceableTracingAspect traceableTracingAspect;

    @Test
    void contextLoads() {
      assertThat(scheduledTracingAspect).isNull();
      assertThat(traceableTracingAspect).isNull();
    }
  }

  @ActiveProfiles(profiles = {"test"})
  @SpringBootTest(
      classes = {TracekitAutoConfiguration.class, TracekitAspectAutoConfiguration.class},
      properties = {"tracekit.aspect.enabled=true", "tracekit.aspect.scheduled-enabled=false"})
  @Nested
  class WithScheduledAspectEnabledFalse {

    @Autowired(required = false)
    ScheduledTracingAspect scheduledTracingAspect;

    @Test
    void contextLoads() {
      assertThat(scheduledTracingAspect).isNull();
    }
  }

  @ActiveProfiles(profiles = {"test"})
  @SpringBootTest(
      classes = {TracekitAutoConfiguration.class, TracekitAspectAutoConfiguration.class},
      properties = {"tracekit.aspect.enabled=true", "tracekit.aspect.traceable-enabled=false"})
  @Nested
  class WithTraceableAspectEnabledFalse {

    @Autowired(required = false)
    TraceableTracingAspect traceableTracingAspect;

    @Test
    void contextLoads() {
      assertThat(traceableTracingAspect).isNull();
    }
  }
}

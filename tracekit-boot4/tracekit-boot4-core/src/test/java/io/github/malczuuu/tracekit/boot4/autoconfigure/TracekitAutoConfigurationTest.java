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
package io.github.malczuuu.tracekit.boot4.autoconfigure;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.malczuuu.tracekit.Tracer;
import io.github.malczuuu.tracekit.boot4.LoggingContextSimpleTracerBuilderCustomizer;
import io.github.malczuuu.tracekit.boot4.TracingTaskDecorator;
import io.github.malczuuu.tracekit.simple.SimpleTracerBuilder;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

class TracekitAutoConfigurationTest {

  @ActiveProfiles(profiles = {"test"})
  @SpringBootTest(
      classes = {TracekitAutoConfiguration.class},
      properties = {"tracekit.enabled=true"})
  @Nested
  class WithEnabledTrue {

    @Autowired(required = false)
    private LoggingContextSimpleTracerBuilderCustomizer loggingContextSimpleTracerBuilderCustomizer;

    @Autowired(required = false)
    private SimpleTracerBuilder simpleTracerBuilder;

    @Autowired(required = false)
    private Tracer tracer;

    @Autowired(required = false)
    private TracingTaskDecorator tracingTaskDecorator;

    @Test
    void contextLoads() {
      assertThat(loggingContextSimpleTracerBuilderCustomizer).isNotNull();
      assertThat(simpleTracerBuilder).isNotNull();
      assertThat(tracer).isNotNull();
      assertThat(tracingTaskDecorator).isNotNull();
    }
  }

  @ActiveProfiles(profiles = {"test"})
  @SpringBootTest(
      classes = {TracekitAutoConfiguration.class},
      properties = {"tracekit.enabled=false"})
  @Nested
  class WithEnabledFalse {

    @Autowired(required = false)
    private LoggingContextSimpleTracerBuilderCustomizer loggingContextSimpleTracerBuilderCustomizer;

    @Autowired(required = false)
    private SimpleTracerBuilder simpleTracerBuilder;

    @Autowired(required = false)
    private Tracer tracer;

    @Autowired(required = false)
    private TracingTaskDecorator tracingTaskDecorator;

    @Test
    void contextLoads() {
      assertThat(loggingContextSimpleTracerBuilderCustomizer).isNull();
      assertThat(simpleTracerBuilder).isNull();
      assertThat(tracer).isNull();
      assertThat(tracingTaskDecorator).isNull();
    }
  }
}

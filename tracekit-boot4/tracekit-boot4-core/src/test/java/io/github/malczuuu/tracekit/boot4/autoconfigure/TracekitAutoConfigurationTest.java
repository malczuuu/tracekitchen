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

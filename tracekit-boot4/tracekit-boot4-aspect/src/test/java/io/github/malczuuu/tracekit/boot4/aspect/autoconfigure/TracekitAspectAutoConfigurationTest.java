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

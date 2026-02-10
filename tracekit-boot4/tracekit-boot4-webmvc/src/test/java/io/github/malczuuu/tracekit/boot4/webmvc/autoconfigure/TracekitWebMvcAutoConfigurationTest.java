package io.github.malczuuu.tracekit.boot4.webmvc.autoconfigure;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.malczuuu.tracekit.boot4.autoconfigure.TracekitAutoConfiguration;
import io.github.malczuuu.tracekit.boot4.webmvc.TracingAwareFilter;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

class TracekitWebMvcAutoConfigurationTest {

  @ActiveProfiles(profiles = {"test"})
  @SpringBootTest(
      classes = {TracekitAutoConfiguration.class, TracekitWebMvcAutoConfiguration.class},
      properties = {"tracekit.enabled=true"})
  @Nested
  class WithTracekitEnabledTrue {

    @Autowired(required = false)
    private TracingAwareFilter tracingAwareFilter;

    @Test
    void contextLoads() {
      assertThat(tracingAwareFilter).isNotNull();
    }
  }

  @ActiveProfiles(profiles = {"test"})
  @SpringBootTest(
      classes = {TracekitAutoConfiguration.class, TracekitWebMvcAutoConfiguration.class},
      properties = {"tracekit.enabled=false"})
  @Nested
  class WithTracekitEnabledFalse {

    @Autowired(required = false)
    private TracingAwareFilter tracingAwareFilter;

    @Test
    void contextLoads() {
      assertThat(tracingAwareFilter).isNull();
    }
  }

  @ActiveProfiles(profiles = {"test"})
  @SpringBootTest(
      classes = {TracekitAutoConfiguration.class, TracekitWebMvcAutoConfiguration.class},
      properties = {"tracekit.webmvc.enabled=true"})
  @Nested
  class WithTracekitWebMvcEnabledTrue {

    @Autowired(required = false)
    private TracingAwareFilter tracingAwareFilter;

    @Test
    void contextLoads() {
      assertThat(tracingAwareFilter).isNotNull();
    }
  }

  @ActiveProfiles(profiles = {"test"})
  @SpringBootTest(
      classes = {TracekitAutoConfiguration.class, TracekitWebMvcAutoConfiguration.class},
      properties = {"tracekit.webmvc.enabled=false"})
  @Nested
  class WithTracekitWebMvcEnabledFalse {

    @Autowired(required = false)
    private TracingAwareFilter tracingAwareFilter;

    @Test
    void contextLoads() {
      assertThat(tracingAwareFilter).isNull();
    }
  }
}

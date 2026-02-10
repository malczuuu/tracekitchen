package io.github.malczuuu.tracekit.spring.restclient.autoconfigure;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.malczuuu.tracekit.spring.autoconfigure.TracekitAutoConfiguration;
import io.github.malczuuu.tracekit.spring.restclient.TracingHttpRequestInterceptor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

class TracekitRestClientAutoConfigurationTest {

  @ActiveProfiles(profiles = {"test"})
  @SpringBootTest(
      classes = {TracekitAutoConfiguration.class, TracekitRestClientAutoConfiguration.class},
      properties = "tracekit.enabled=true")
  @Nested
  class WithTracekitEnabledTrue {

    @Autowired(required = false)
    private TracingHttpRequestInterceptor tracingHttpRequestInterceptor;

    @Test
    void contextLoads() {
      assertThat(tracingHttpRequestInterceptor).isNotNull();
    }
  }

  @ActiveProfiles(profiles = {"test"})
  @SpringBootTest(
      classes = {TracekitAutoConfiguration.class, TracekitRestClientAutoConfiguration.class},
      properties = "tracekit.enabled=false")
  @Nested
  class WithTracekitEnabledFalse {

    @Autowired(required = false)
    private TracingHttpRequestInterceptor tracingHttpRequestInterceptor;

    @Test
    void contextLoads() {
      assertThat(tracingHttpRequestInterceptor).isNull();
    }
  }

  @ActiveProfiles(profiles = {"test"})
  @SpringBootTest(
      classes = {TracekitAutoConfiguration.class, TracekitRestClientAutoConfiguration.class},
      properties = "tracekit.restclient.enabled=true")
  @Nested
  class WithTracekitRestClientEnabledTrue {

    @Autowired(required = false)
    private TracingHttpRequestInterceptor tracingHttpRequestInterceptor;

    @Test
    void contextLoads() {
      assertThat(tracingHttpRequestInterceptor).isNotNull();
    }
  }

  @ActiveProfiles(profiles = {"test"})
  @SpringBootTest(
      classes = {TracekitAutoConfiguration.class, TracekitRestClientAutoConfiguration.class},
      properties = "tracekit.restclient.enabled=false")
  @Nested
  class WithTracekitRestClientEnabledFalse {

    @Autowired(required = false)
    private TracingHttpRequestInterceptor tracingHttpRequestInterceptor;

    @Test
    void contextLoads() {
      assertThat(tracingHttpRequestInterceptor).isNull();
    }
  }
}

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

package io.github.malczuuu.tracekit.boot4.restclient.autoconfigure;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.malczuuu.tracekit.boot4.autoconfigure.TracekitAutoConfiguration;
import io.github.malczuuu.tracekit.boot4.restclient.TracingHttpRequestInterceptor;
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

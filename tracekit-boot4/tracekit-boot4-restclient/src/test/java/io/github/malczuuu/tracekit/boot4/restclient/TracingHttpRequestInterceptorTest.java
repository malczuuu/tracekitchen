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

package io.github.malczuuu.tracekit.boot4.restclient;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.malczuuu.tracekit.OpenSpan;
import io.github.malczuuu.tracekit.Span;
import io.github.malczuuu.tracekit.Tracer;
import java.net.URI;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestOperations;

@ActiveProfiles(profiles = {"test"})
@NullUnmarked
@SpringBootTest(
    classes = {TracingHttpRequestInterceptorTest.CurrentTestConfiguration.class},
    properties = {
      "tracekit.trace-id-header-names=X-Test-Trace-Id",
      "tracekit.span-id-header-names=X-Test-Span-Id",
      "tracekit.parent-span-id-header-names=X-Test-Parent-Span-Id"
    },
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TracingHttpRequestInterceptorTest {

  @LocalServerPort private int port;

  @Autowired private DummyController dummyController;

  @Autowired private Tracer tracer;

  @Autowired private RestClient restClient;

  @Autowired private RestOperations restOperations;

  @AfterEach
  void afterEach() {
    dummyController.xTraceId = null;
    dummyController.xSpanId = null;
    dummyController.xParentSpanId = null;
  }

  @Test
  void givenOpenContext_whenCallingWithRestClient_shouldPropagateIt() {
    Span span = tracer.root().spawnChild();

    try (OpenSpan o = span.open()) {
      restClient.get().uri("http://localhost:" + port + "/dummy").retrieve().toEntity(String.class);

      assertThat(dummyController.xTraceId).isEqualTo(span.getTrace().getTraceId());
      assertThat(dummyController.xSpanId).isEqualTo(span.getTrace().getSpanId());
      assertThat(dummyController.xParentSpanId).isEqualTo(span.getTrace().getParentSpanId());
    }
  }

  @Test
  void givenOpenContext_whenCallingWithRestOperations_shouldPropagateIt() {
    Span span = tracer.root().spawnChild();

    try (OpenSpan o = span.open()) {
      restOperations.getForEntity(URI.create("http://localhost:" + port + "/dummy"), String.class);

      assertThat(dummyController.xTraceId).isEqualTo(span.getTrace().getTraceId());
      assertThat(dummyController.xSpanId).isEqualTo(span.getTrace().getSpanId());
      assertThat(dummyController.xParentSpanId).isEqualTo(span.getTrace().getParentSpanId());
    }
  }

  @EnableAutoConfiguration
  static class CurrentTestConfiguration {

    @Bean
    DummyController dummyController() {
      return new DummyController();
    }

    @Bean
    RestClient restClient(RestClient.Builder builder) {
      return builder.build();
    }

    @Bean
    RestOperations restOperations(RestTemplateBuilder builder) {
      return builder.build();
    }
  }

  @RestController
  static class DummyController {

    String xTraceId;
    String xSpanId;
    String xParentSpanId;

    @GetMapping(path = "/dummy")
    public String dummy(
        @RequestHeader("X-Test-Trace-Id") String xTraceId,
        @RequestHeader("X-Test-Span-Id") String xSpanId,
        @RequestHeader("X-Test-Parent-Span-Id") String xParentSpanId) {
      this.xTraceId = xTraceId;
      this.xSpanId = xSpanId;
      this.xParentSpanId = xParentSpanId;

      return "OK";
    }
  }
}

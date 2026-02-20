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
package io.github.malczuuu.tracekit.boot4.webmvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.github.malczuuu.tracekit.Span;
import io.github.malczuuu.tracekit.Tracer;
import io.github.malczuuu.tracekit.boot4.autoconfigure.TracekitAutoConfiguration;
import io.github.malczuuu.tracekit.boot4.webmvc.autoconfigure.TracekitWebMvcAutoConfiguration;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@ActiveProfiles(profiles = {"test"})
@NullUnmarked
@SpringBootTest(
    classes = {
      TracekitAutoConfiguration.class,
      TracekitWebMvcAutoConfiguration.class,
      TracingAwareFilterTest.DummyController.class
    },
    properties = {
      "tracekit.trace-id-header-names=X-Test-Trace-Id",
      "tracekit.span-id-header-names=X-Test-Span-Id",
      "tracekit.parent-span-id-header-names=X-Test-Parent-Span-Id"
    })
@AutoConfigureMockMvc
class TracingAwareFilterTest {

  @Autowired private DummyController dummyController;
  @Autowired private MockMvc mockMvc;

  @BeforeEach
  void afterEach() {
    dummyController.span = null;
  }

  @Test
  void givenRequestWithHeaders_whenApiCall_shouldConstructSpan() throws Exception {
    mockMvc
        .perform(
            get("/dummy")
                .header("X-Test-Trace-Id", "x-test-trace-id-value")
                .header("X-Test-Span-Id", "x-test-span-id-value")
                .header("X-Test-Parent-Span-Id", "x-test-parent-span-id-value"))
        .andExpect(status().isOk());

    assertThat(dummyController.span.getTrace().getTraceId()).isEqualTo("x-test-trace-id-value");
    assertThat(dummyController.span.getTrace().getSpanId()).isNotNull();
    assertThat(dummyController.span.getTrace().getParentSpanId()).isEqualTo("x-test-span-id-value");
  }

  @RestController
  static class DummyController {

    private final Tracer tracer;
    private Span span;

    DummyController(Tracer tracer) {
      this.tracer = tracer;
    }

    @GetMapping(path = "/dummy", produces = MediaType.TEXT_PLAIN_VALUE)
    public String dummy() {
      span = tracer.getCurrentSpan();
      return "OK";
    }
  }
}

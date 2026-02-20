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

import io.github.malczuuu.tracekit.Span;
import io.github.malczuuu.tracekit.Tracer;
import io.github.malczuuu.tracekit.simple.SimpleTracer;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.mock.web.MockHttpServletRequest;

class ServletRequestExtractorTest {

  private ServletRequestExtractor servletRequestExtractor;

  @BeforeEach
  void beforeEach() {
    Tracer tracer = SimpleTracer.create();
    servletRequestExtractor =
        new ServletRequestExtractor(
            tracer,
            new DummyHeaderSettings(
                List.of("X-Test-Trace-Id", "X-Test2-Trace-Id"),
                List.of("X-Test-Span-Id", "X-Test2-Span-Id"),
                List.of("X-Test-Parent-Span-Id", "X-Test2-Parent-Span-Id")));
  }

  @ParameterizedTest
  @CsvSource({
    "X-Test-Trace-Id , X-Test-Span-Id,  X-Test-Parent-Span-Id,  x-test-trace-id-value,  x-test-span-id-value,  x-test-parent-span-id-value",
    "X-Test2-Trace-Id, X-Test2-Span-Id, X-Test2-Parent-Span-Id, x-test2-trace-id-value, x-test2-span-id-value, x-test2-parent-span-id-value",
    "X-Test2-Trace-Id, X-Test-Span-Id,  X-Test-Parent-Span-Id,  x-test2-trace-id-value, x-test-span-id-value,  x-test-parent-span-id-value",
    "X-Test-Trace-Id,  X-Test2-Span-Id, X-Test2-Parent-Span-Id, x-test-trace-id-value,  x-test2-span-id-value, x-test2-parent-span-id-value",
    "X-Test2-Trace-Id, X-Test2-Span-Id, X-Test-Parent-Span-Id,  x-test2-trace-id-value, x-test2-span-id-value, x-test-parent-span-id-value"
  })
  void givenRequest_whenExtractingSpan_shouldHonorHeaders(
      String traceHeader,
      String spanHeader,
      String parentHeader,
      String traceValue,
      String spanValue,
      String parentValue) {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader(traceHeader, traceValue);
    request.addHeader(spanHeader, spanValue);
    request.addHeader(parentHeader, parentValue);

    Optional<Span> optionalSpan = servletRequestExtractor.extract(request);
    assertThat(optionalSpan).isPresent();

    Span span = optionalSpan.get();
    assertThat(span.getTrace().getTraceId()).isEqualTo(traceValue);
    assertThat(span.getTrace().getSpanId()).isEqualTo(spanValue);
    assertThat(span.getTrace().getParentSpanId()).isEqualTo(parentValue);
  }

  @Test
  void givenRequestWithoutParentSpanId_whenExtractingSpan_shouldHonorHeaders() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("X-Test-Trace-Id", "x-test-trace-id-value");
    request.addHeader("X-Test-Span-Id", "x-test-span-id-value");

    Optional<Span> optionalSpan = servletRequestExtractor.extract(request);
    assertThat(optionalSpan).isPresent();

    Span span = optionalSpan.get();
    assertThat(span.getTrace().getTraceId()).isEqualTo("x-test-trace-id-value");
    assertThat(span.getTrace().getSpanId()).isEqualTo("x-test-span-id-value");
    assertThat(span.getTrace().getParentSpanId()).isNull();
  }

  @Test
  void givenRequestWithoutSpanId_whenExtractingSpan_shouldHonorHeader() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("X-Test-Trace-Id", "x-test-trace-id-value");
    request.addHeader("X-Test-Parent-Span-Id", "x-test-parent-span-id-value");

    Optional<Span> optionalSpan = servletRequestExtractor.extract(request);
    assertThat(optionalSpan).isPresent();

    Span span = optionalSpan.get();
    assertThat(span.getTrace().getTraceId()).isEqualTo("x-test-trace-id-value");
    assertThat(span.getTrace().getSpanId()).isNotNull();
    assertThat(span.getTrace().getSpanId()).isNotEqualTo("x-test-span-id-value");
    assertThat(span.getTrace().getParentSpanId()).isNull();
  }

  @Test
  void givenRequestWithoutTraceId_whenExtractingSpan_shouldNotGenerate() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("X-Test-Span-Id", "x-test-span-id-value");

    Optional<Span> optionalSpan = servletRequestExtractor.extract(request);
    assertThat(optionalSpan).isEmpty();
  }
}

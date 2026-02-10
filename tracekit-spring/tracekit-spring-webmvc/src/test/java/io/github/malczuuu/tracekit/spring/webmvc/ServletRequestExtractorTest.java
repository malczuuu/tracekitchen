package io.github.malczuuu.tracekit.spring.webmvc;

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
  void givenRequestWithoutSpanId_whenExtractingSpan_shouldNotGenerate() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("X-Test-Trace-Id", "x-test-trace-id-value");

    Optional<Span> optionalSpan = servletRequestExtractor.extract(request);
    assertThat(optionalSpan).isEmpty();
  }

  @Test
  void givenRequestWithoutTraceId_whenExtractingSpan_shouldNotGenerate() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("X-Test-Span-Id", "x-test-span-id-value");

    Optional<Span> optionalSpan = servletRequestExtractor.extract(request);
    assertThat(optionalSpan).isEmpty();
  }
}

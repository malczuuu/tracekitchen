package io.github.malczuuu.tracekit.boot4;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.malczuuu.tracekit.Span;
import io.github.malczuuu.tracekit.Tracer;
import io.github.malczuuu.tracekit.simple.SimpleTracer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

class LoggingContextLifecycleAdapterTest {

  private Tracer tracer;
  private LoggingContextLifecycleAdapter lifecycleAdapter;

  @BeforeEach
  void beforeEach() {
    tracer = SimpleTracer.create();
    lifecycleAdapter = new LoggingContextLifecycleAdapter();
  }

  @AfterEach
  void afterEach() {
    MDC.clear();
  }

  @Test
  void givenSpan_whenTriggeringAfterOpened_shouldAssignMDC() {
    Span span = tracer.root().spawnChild();

    lifecycleAdapter.afterOpened(span, null);

    assertThat(MDC.get("traceId")).isEqualTo(span.getTrace().getTraceId());
    assertThat(MDC.get("spanId")).isEqualTo(span.getTrace().getSpanId());
    assertThat(MDC.get("parentSpanId")).isEqualTo(span.getTrace().getParentSpanId());
  }

  @Test
  void givenSpan_whenTriggeringAfterClosed_shouldUnassignMDC() {
    Span span = tracer.root().spawnChild();

    lifecycleAdapter.afterOpened(span, null);

    assertThat(MDC.get("traceId")).isEqualTo(span.getTrace().getTraceId());
    assertThat(MDC.get("spanId")).isEqualTo(span.getTrace().getSpanId());
    assertThat(MDC.get("parentSpanId")).isEqualTo(span.getTrace().getParentSpanId());

    lifecycleAdapter.afterClosed(span, null);

    assertThat(MDC.get("traceId")).isNull();
    assertThat(MDC.get("spanId")).isNull();
    assertThat(MDC.get("parentSpanId")).isNull();
  }

  @Test
  void givenPrevious_whenTriggeringAfterClosed_shouldRestoreMDC() {
    Span previous = tracer.root().spawnChild();
    Span span = tracer.root().spawnChild();

    lifecycleAdapter.afterOpened(span, null);

    assertThat(MDC.get("traceId")).isEqualTo(span.getTrace().getTraceId());
    assertThat(MDC.get("spanId")).isEqualTo(span.getTrace().getSpanId());
    assertThat(MDC.get("parentSpanId")).isEqualTo(span.getTrace().getParentSpanId());

    lifecycleAdapter.afterClosed(span, previous);

    assertThat(MDC.get("traceId")).isEqualTo(previous.getTrace().getTraceId());
    assertThat(MDC.get("spanId")).isEqualTo(previous.getTrace().getSpanId());
    assertThat(MDC.get("parentSpanId")).isEqualTo(previous.getTrace().getParentSpanId());
  }
}

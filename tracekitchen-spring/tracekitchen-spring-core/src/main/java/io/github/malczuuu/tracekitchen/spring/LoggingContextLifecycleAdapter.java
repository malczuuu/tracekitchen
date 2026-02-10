package io.github.malczuuu.tracekitchen.spring;

import io.github.malczuuu.tracekitchen.Span;
import io.github.malczuuu.tracekitchen.SpanLifecycleAdapter;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class LoggingContextLifecycleAdapter implements SpanLifecycleAdapter {

  private static final Logger log = LoggerFactory.getLogger(LoggingContextLifecycleAdapter.class);

  @Override
  public void afterOpened(Span span, @Nullable Span previousSpan) {
    set(span);
    log.debug("Context [{}] opened", span.getName());
  }

  @Override
  public void afterClosed(Span span, @Nullable Span currentSpan) {
    log.debug(
        "Context [{}] closed; durationMilli={}", span.getName(), span.getDuration().toMillis());
    if (currentSpan != null) {
      set(currentSpan);
    } else {
      drop();
    }
  }

  private void set(Span current) {
    MDC.put("traceId", current.getTrace().getTraceId());
    MDC.put("spanId", current.getTrace().getSpanId());
    if (current.getTrace().getParentSpanId() != null) {
      MDC.put("parentSpanId", current.getTrace().getParentSpanId());
    } else {
      MDC.remove("parentSpanId");
    }
  }

  private void drop() {
    MDC.remove("traceId");
    MDC.remove("spanId");
    MDC.remove("parentSpanId");
  }
}

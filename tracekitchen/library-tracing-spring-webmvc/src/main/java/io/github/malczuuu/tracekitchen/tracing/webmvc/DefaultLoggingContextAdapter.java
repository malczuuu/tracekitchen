package io.github.malczuuu.tracekitchen.tracing.webmvc;

import io.github.malczuuu.tracekitchen.tracing.core.LoggingContextAdapter;
import io.github.malczuuu.tracekitchen.tracing.core.TraceContext;
import org.slf4j.MDC;

public class DefaultLoggingContextAdapter implements LoggingContextAdapter {

  @Override
  public void push(TraceContext context) {
    MDC.put("traceId", context.getTraceId());
    MDC.put("spanId", context.getSpanId());
  }

  @Override
  public void drop() {
    MDC.remove("traceId");
    MDC.remove("spanId");
  }
}

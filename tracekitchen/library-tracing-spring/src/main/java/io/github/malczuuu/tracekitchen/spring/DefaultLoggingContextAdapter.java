package io.github.malczuuu.tracekitchen.spring;

import io.github.malczuuu.tracekitchen.LoggingContextAdapter;
import io.github.malczuuu.tracekitchen.TraceContext;
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

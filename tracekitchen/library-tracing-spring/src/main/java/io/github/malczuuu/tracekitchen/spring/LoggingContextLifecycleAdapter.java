package io.github.malczuuu.tracekitchen.spring;

import io.github.malczuuu.tracekitchen.TraceContext;
import io.github.malczuuu.tracekitchen.TraceContextLifecycleAdapter;
import org.jspecify.annotations.Nullable;
import org.slf4j.MDC;

public class LoggingContextLifecycleAdapter implements TraceContextLifecycleAdapter {

  @Override
  public void afterOpened(TraceContext context, @Nullable TraceContext previousContext) {
    set(context);
  }

  @Override
  public void afterClosed(TraceContext context, @Nullable TraceContext currentContext) {
    if (currentContext != null) {
      set(currentContext);
    } else {
      drop();
    }
  }

  private void set(TraceContext current) {
    MDC.put("traceId", current.getTraceId());
    MDC.put("spanId", current.getSpanId());
    MDC.put("parentSpanId", current.getParentSpanId());
  }

  private void drop() {
    MDC.remove("traceId");
    MDC.remove("spanId");
    MDC.remove("parentSpanId");
  }
}

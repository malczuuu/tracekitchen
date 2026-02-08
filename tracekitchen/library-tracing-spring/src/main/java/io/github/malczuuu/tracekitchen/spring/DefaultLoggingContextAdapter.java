package io.github.malczuuu.tracekitchen.spring;

import io.github.malczuuu.tracekitchen.TraceContext;
import io.github.malczuuu.tracekitchen.TraceContextLifecycleAdapter;
import io.github.malczuuu.tracekitchen.Tracer;
import org.slf4j.MDC;

public class DefaultLoggingContextAdapter implements TraceContextLifecycleAdapter {

  private final Tracer tracer;

  public DefaultLoggingContextAdapter(Tracer tracer) {
    this.tracer = tracer;
  }

  @Override
  public void afterOpened(TraceContext context) {
    set(context);
  }

  @Override
  public void afterClosed(TraceContext context) {
    TraceContext current = tracer.getCurrentContext();
    if (current != null) {
      set(current);
    } else {
      drop();
    }
  }

  private void set(TraceContext current) {
    MDC.put("traceId", current.getTraceId());
    MDC.put("spanId", current.getSpanId());
  }

  private void drop() {
    MDC.remove("traceId");
    MDC.remove("spanId");
  }
}

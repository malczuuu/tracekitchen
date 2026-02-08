package io.github.malczuuu.tracekitchen.spring;

import io.github.malczuuu.tracekitchen.TraceContext;
import io.github.malczuuu.tracekitchen.TraceContextLifecycleAdapter;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class LoggingContextLifecycleAdapter implements TraceContextLifecycleAdapter {

  private static final Logger log = LoggerFactory.getLogger(LoggingContextLifecycleAdapter.class);

  @Override
  public void afterOpened(TraceContext context, @Nullable TraceContext previousContext) {
    set(context);
    log.debug("Context [{}] opened", context.getName());
  }

  @Override
  public void afterClosed(TraceContext context, @Nullable TraceContext currentContext) {
    log.debug(
        "Context [{}] closed; durationMilli={}",
        context.getName(),
        context.getDuration().toMillis());
    if (currentContext != null) {
      set(currentContext);
    } else {
      drop();
    }
  }

  private void set(TraceContext current) {
    MDC.put("traceId", current.getTraceId());
    MDC.put("spanId", current.getSpanId());
    if (current.getParentSpanId() != null) {
      MDC.put("parentSpanId", current.getParentSpanId());
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

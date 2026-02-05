package io.github.malczuuu.tracekitchen.tracing.impl;

import io.github.malczuuu.tracekitchen.tracing.api.Context;
import io.github.malczuuu.tracekitchen.tracing.api.OpenContext;
import io.github.malczuuu.tracekitchen.tracing.api.Tracer;
import org.slf4j.MDC;

public class SimpleTracer implements Tracer {

  @Override
  public Context createContext() {
    return new SimpleContext();
  }

  @Override
  public OpenContext openContext(Context context) {
    ContextHolder.push(context);
    mdc();

    return new OpenContextWrapper(
        context,
        () -> {
          ContextHolder.pop();
          mdc();
        });
  }

  @Override
  public Context getCurrentContext() {
    return ContextHolder.current();
  }

  private void mdc() {
    Context ctx = ContextHolder.current();
    if (ctx != null) {
      MDC.put("traceId", ctx.getTraceId());
      MDC.put("spanId", ctx.getSpanId());
    } else {
      MDC.remove("traceId");
      MDC.remove("spanId");
    }
  }
}

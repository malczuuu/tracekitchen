package io.github.malczuuu.tracekit.app.entrypoint;

import io.github.malczuuu.tracekit.Span;
import io.github.malczuuu.tracekit.TraceScope;
import io.github.malczuuu.tracekit.Traceable;
import io.github.malczuuu.tracekit.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Traceable(scope = TraceScope.REQUIRED)
public class TracedService {

  private static final Logger log = LoggerFactory.getLogger(TracedService.class);

  private final Tracer tracer;

  public TracedService(Tracer tracer) {
    this.tracer = tracer;
  }

  @Traceable(scope = TraceScope.REQUIRED)
  public void withMethodRequired() {
    Span span = tracer.getCurrentSpan();
    log.atInfo()
        .addKeyValue("spanName", span.getName())
        .log("executing method traced with method-level @Traceable(REQUIRED)");
  }

  @Traceable(scope = TraceScope.REQUIRES_NEW)
  public void withMethodRequiresNew() {
    Span span = tracer.getCurrentSpan();
    log.atInfo()
        .addKeyValue("spanName", span.getName())
        .log("executing method traced with method-level @Traceable(REQUIRES_NEW)");
  }

  public void withClassRequired() {
    Span span = tracer.getCurrentSpan();
    log.atInfo()
        .addKeyValue("spanName", span.getName())
        .log("executing method traced with class-level @Traceable(REQUIRED)");
  }
}

package io.github.malczuuu.tracekit.app.entrypoint;

import io.github.malczuuu.tracekit.Span;
import io.github.malczuuu.tracekit.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncService {

  private static final Logger log = LoggerFactory.getLogger(AsyncService.class);

  private final Tracer tracer;

  public AsyncService(Tracer tracer) {
    this.tracer = tracer;
  }

  @Async
  public void async() {
    Span span = tracer.getCurrentSpan();
    log.atInfo().addKeyValue("spanName", span.getName()).log("executing async task");
  }

  @Async("queueTaskExecutor")
  public void queuedAsync() {
    Span span = tracer.getCurrentSpan();
    log.atInfo().addKeyValue("spanName", span.getName()).log("executing queued async task");
  }
}

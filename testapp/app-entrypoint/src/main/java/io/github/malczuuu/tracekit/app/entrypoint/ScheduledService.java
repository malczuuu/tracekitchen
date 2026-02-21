package io.github.malczuuu.tracekit.app.entrypoint;

import io.github.malczuuu.tracekit.Span;
import io.github.malczuuu.tracekit.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledService {

  private static final Logger log = LoggerFactory.getLogger(ScheduledService.class);

  private final Tracer tracer;

  public ScheduledService(Tracer tracer) {
    this.tracer = tracer;
  }

  @Scheduled(fixedRateString = "5s", initialDelayString = "3s")
  public void run() {
    Span span = tracer.getCurrentSpan();
    log.atInfo().addKeyValue("spanName", span.getName()).log("executing scheduled task");
  }
}

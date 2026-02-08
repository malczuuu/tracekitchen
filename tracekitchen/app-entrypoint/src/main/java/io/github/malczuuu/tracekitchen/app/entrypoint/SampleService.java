package io.github.malczuuu.tracekitchen.app.entrypoint;

import io.github.malczuuu.tracekitchen.annotation.TraceScope;
import io.github.malczuuu.tracekitchen.annotation.Traceable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Traceable(scope = TraceScope.REQUIRED)
@Component
public class SampleService {

  private static final Logger log = LoggerFactory.getLogger(SampleService.class);

  @Traceable(scope = TraceScope.REQUIRED)
  public void requires() {
    log.info("requires");
  }

  @Traceable(scope = TraceScope.REQUIRES_NEW)
  public void requiresNew() {
    log.info("requiresNew");
  }

  public void classAware() {
    log.info("classAware");
  }
}

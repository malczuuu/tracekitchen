package io.github.malczuuu.tracekit.app.entrypoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AsyncService {

  private static final Logger log = LoggerFactory.getLogger(AsyncService.class);

  public AsyncService(ObjectProvider<AsyncConfigurer> configurers) {
    log.info("A");
  }

  @Async
  public void async() {
    log.info("async");
  }

  @Scheduled(fixedRateString = "5s", initialDelayString = "3s")
  public void scheduled() {
    log.info("scheduled");
  }
}

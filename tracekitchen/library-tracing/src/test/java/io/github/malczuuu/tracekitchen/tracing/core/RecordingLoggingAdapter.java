package io.github.malczuuu.tracekitchen.tracing.core;

import java.util.concurrent.atomic.AtomicInteger;
import org.jspecify.annotations.NullMarked;

@NullMarked
class RecordingLoggingAdapter implements LoggingContextAdapter {

  private final AtomicInteger pushHits;
  private final AtomicInteger dropHits;

  RecordingLoggingAdapter(AtomicInteger pushHits, AtomicInteger dropHits) {
    this.pushHits = pushHits;
    this.dropHits = dropHits;
  }

  @Override
  public void push(TraceContext context) {
    pushHits.incrementAndGet();
  }

  @Override
  public void drop() {
    dropHits.incrementAndGet();
  }
}

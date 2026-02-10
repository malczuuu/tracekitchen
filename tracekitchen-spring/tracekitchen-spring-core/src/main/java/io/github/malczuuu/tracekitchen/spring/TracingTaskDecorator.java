package io.github.malczuuu.tracekitchen.spring;

import io.github.malczuuu.tracekitchen.OpenSpan;
import io.github.malczuuu.tracekitchen.Span;
import io.github.malczuuu.tracekitchen.Tracer;
import org.springframework.core.task.TaskDecorator;

public class TracingTaskDecorator implements TaskDecorator {

  private final Tracer tracer;

  public TracingTaskDecorator(Tracer tracer) {
    this.tracer = tracer;
  }

  @Override
  public Runnable decorate(Runnable runnable) {
    Span span = tracer.getCurrentSpan();
    if (span == null) {
      return runnable;
    }
    return () -> {
      Span child = span.spawnChild(span.getName() + " [subroutine]");
      try (OpenSpan open = child.open()) {
        runnable.run();
      }
    };
  }
}

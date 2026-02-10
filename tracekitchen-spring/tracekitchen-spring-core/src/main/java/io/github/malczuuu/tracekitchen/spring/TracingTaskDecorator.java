package io.github.malczuuu.tracekitchen.spring;

import io.github.malczuuu.tracekitchen.OpenContext;
import io.github.malczuuu.tracekitchen.TraceContext;
import io.github.malczuuu.tracekitchen.TraceContextSnapshot;
import io.github.malczuuu.tracekitchen.Tracer;
import org.springframework.core.task.TaskDecorator;

public class TracingTaskDecorator implements TaskDecorator {

  private final Tracer tracer;

  public TracingTaskDecorator(Tracer tracer) {
    this.tracer = tracer;
  }

  @Override
  public Runnable decorate(Runnable runnable) {
    TraceContextSnapshot context = tracer.getCurrentContext();
    if (context == null) {
      return runnable;
    }
    return () -> {
      TraceContext child = context.makeChild(context.getName() + " [subroutine]");
      try (OpenContext open = child.open()) {
        runnable.run();
      }
    };
  }
}

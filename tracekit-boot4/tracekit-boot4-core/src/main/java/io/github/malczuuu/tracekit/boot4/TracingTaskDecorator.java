/*
 * Copyright (c) 2026 Damian Malczewski
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.github.malczuuu.tracekit.boot4;

import io.github.malczuuu.tracekit.OpenSpan;
import io.github.malczuuu.tracekit.Span;
import io.github.malczuuu.tracekit.Tracer;
import org.springframework.core.task.TaskDecorator;

/**
 * {@code TracingTaskDecorator} is a Spring {@link TaskDecorator} that propagates the current {@link
 * Span} context to a {@link Runnable} and creates a child span for its execution.
 *
 * <p>When applied to a task (e.g., for use with {@code @Async} or scheduled tasks), it ensures that
 * the tracing context is maintained and that a new child span is created for the task's execution,
 * allowing proper correlation of logs and traces.
 *
 * <p>If there is no current span, the runnable is executed as-is without creating a new span.
 */
public class TracingTaskDecorator implements TaskDecorator {

  private final Tracer tracer;

  /**
   * Creates a new {@code TracingTaskDecorator} that uses the given {@link Tracer}.
   *
   * @param tracer the tracer used to obtain the current span and create child spans
   */
  public TracingTaskDecorator(Tracer tracer) {
    this.tracer = tracer;
  }

  /**
   * Decorates the given {@link Runnable} by wrapping it in a child span of the current span. The
   * child span is opened before the runnable executes and closed automatically after execution.
   *
   * @param runnable the original {@link Runnable} task
   * @return a decorated {@link Runnable} that runs within a child span if a current span exists, or
   *     the original runnable if there is no active span
   */
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

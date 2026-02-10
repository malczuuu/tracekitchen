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
package io.github.malczuuu.tracekit.boot4.aspect;

import io.github.malczuuu.tracekit.OpenSpan;
import io.github.malczuuu.tracekit.Span;
import io.github.malczuuu.tracekit.Tracer;
import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Aspect that automatically propagates a {@link Span} for methods annotated with {@code Scheduled}.
 *
 * <p>This aspect intercepts execution of scheduled methods and ensures that a {@link Span} is
 * available during the method's execution. If no span is active, a new root span is created. If a
 * span already exists, a child span is created to maintain proper tracing hierarchy.
 *
 * <p>The span is automatically closed at the end of the method via try-with-resources, ensuring
 * that any previous span is restored.
 *
 * <p>This allows scheduled tasks to participate in distributed tracing without requiring explicit
 * span management in the task implementation.
 *
 * @see org.springframework.scheduling.annotation.Scheduled
 */
@Aspect
public class ScheduledTracingAspect {

  private final Tracer tracer;

  /**
   * Constructs the aspect with the provided {@link Tracer}.
   *
   * @param tracer the tracer used to create and open trace contexts
   */
  public ScheduledTracingAspect(Tracer tracer) {
    this.tracer = tracer;
  }

  /**
   * Around advice that wraps execution of methods annotated with {@code Scheduled}.
   *
   * <p>If there is an active {@link Span}, a child span is created. Otherwise, a new root span is
   * created. The span is opened before method execution and automatically closed to restore any
   * previous span.
   *
   * @param joinPoint the join point representing the intercepted scheduled method
   * @return the method's return value
   * @throws Throwable if the intercepted method throws
   * @see org.springframework.scheduling.annotation.Scheduled
   */
  @Around("@annotation(org.springframework.scheduling.annotation.Scheduled)")
  public Object aroundScheduled(ProceedingJoinPoint joinPoint) throws Throwable {
    Span parent = tracer.getCurrentSpan();

    Span span =
        parent == null
            ? tracer.root(findMethodName(joinPoint))
            : parent.spawnChild(findMethodName(joinPoint));

    try (OpenSpan open = span.open()) {
      return joinPoint.proceed();
    }
  }

  /**
   * Builds a default method name for the trace span using the declaring class and method name.
   *
   * @param joinPoint the intercepted join point
   * @return the default span name
   */
  protected String findMethodName(ProceedingJoinPoint joinPoint) {
    MethodSignature sig = (MethodSignature) joinPoint.getSignature();
    Method method = sig.getMethod();
    return method.getDeclaringClass().getSimpleName() + "." + method.getName();
  }
}

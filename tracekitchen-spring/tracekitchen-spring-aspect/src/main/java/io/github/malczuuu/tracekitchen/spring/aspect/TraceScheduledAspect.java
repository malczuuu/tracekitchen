package io.github.malczuuu.tracekitchen.spring.aspect;

import io.github.malczuuu.tracekitchen.OpenSpan;
import io.github.malczuuu.tracekitchen.Span;
import io.github.malczuuu.tracekitchen.Tracer;
import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Aspect that automatically propagates a {@link Span} for methods annotated with {@code Scheduled}.
 *
 * <p>This aspect intercepts execution of scheduled methods and ensures that a {@link Span} is
 * available during the method's execution. If no context is active, a new root context is created.
 * If a context already exists, a child span is created to maintain proper tracing hierarchy.
 *
 * <p>The context is automatically closed at the end of the method via try-with-resources, ensuring
 * that any previous context is restored.
 *
 * <p>This allows scheduled tasks to participate in distributed tracing without requiring explicit
 * context management in the task implementation.
 *
 * @see org.springframework.scheduling.annotation.Scheduled
 */
@Aspect
public class TraceScheduledAspect {

  private final Tracer tracer;

  /**
   * Constructs the aspect with the provided {@link Tracer}.
   *
   * @param tracer the tracer used to create and open trace contexts
   */
  public TraceScheduledAspect(Tracer tracer) {
    this.tracer = tracer;
  }

  /**
   * Around advice that wraps execution of methods annotated with {@code Scheduled}.
   *
   * <p>If there is an active {@link Span}, a child span is created. Otherwise, a new root context
   * is created. The context is opened before method execution and automatically closed to restore
   * any previous context.
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
   * Builds a default method name for the trace context using the declaring class and method name.
   *
   * @param joinPoint the intercepted join point
   * @return the default context name
   */
  protected String findMethodName(ProceedingJoinPoint joinPoint) {
    MethodSignature sig = (MethodSignature) joinPoint.getSignature();
    Method method = sig.getMethod();
    return method.getDeclaringClass().getSimpleName() + "." + method.getName();
  }
}

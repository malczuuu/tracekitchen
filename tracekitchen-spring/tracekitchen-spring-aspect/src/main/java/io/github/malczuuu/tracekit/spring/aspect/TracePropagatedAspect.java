package io.github.malczuuu.tracekit.spring.aspect;

import io.github.malczuuu.tracekit.OpenSpan;
import io.github.malczuuu.tracekit.Span;
import io.github.malczuuu.tracekit.TraceScope;
import io.github.malczuuu.tracekit.Traceable;
import io.github.malczuuu.tracekit.Tracer;
import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.jspecify.annotations.Nullable;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.StringUtils;

/**
 * Aspect that automatically propagates a {@link Span} for methods or classes annotated with {@link
 * Traceable}.
 *
 * <p>The aspect intercepts calls to methods annotated with {@code @Traceable} or classes annotated
 * with {@code @Traceable}, and ensures that a {@link Span} is properly created and opened for the
 * duration of the method execution.
 *
 * <p>If a parent span exists and the {@link TraceScope} is {@code REQUIRED}, a child span is
 * created. If {@link TraceScope#REQUIRES_NEW} is used, a new root span is created, ignoring any
 * existing parent span.
 *
 * <p>The span is automatically closed at the end of the method via {@code try-with-resources},
 * ensuring proper restoration of any previous span.
 */
@Aspect
public class TracePropagatedAspect {

  private final Tracer tracer;

  /**
   * Constructs the aspect with the provided {@link Tracer}.
   *
   * @param tracer the tracer used to create and open trace contexts
   */
  public TracePropagatedAspect(Tracer tracer) {
    this.tracer = tracer;
  }

  /**
   * Around advice that wraps execution of a method annotated with {@link Traceable} or within a
   * class annotated with {@link Traceable}.
   *
   * <p>Depending on the {@link TraceScope}:
   *
   * <ul>
   *   <li>{@link TraceScope#REQUIRED}: uses the current span if available, creating a child span if
   *       a parent exists, otherwise creates a new root span.
   *   <li>{@link TraceScope#REQUIRES_NEW}: always creates a new root span.
   * </ul>
   *
   * <p>The created span is opened before method execution and automatically closed.
   *
   * @param joinPoint the join point representing the intercepted method
   * @return the method's return value
   * @throws Throwable if the intercepted method throws
   */
  @Around(
      "@annotation(io.github.malczuuu.tracekit.Traceable) || @within(io.github.malczuuu.tracekit.Traceable)")
  public Object aroundTraceable(ProceedingJoinPoint joinPoint) throws Throwable {
    Traceable traceable = findAnnotation(joinPoint);

    if (traceable == null) {
      return joinPoint.proceed();
    }

    String contextName = findContextName(joinPoint, traceable);

    Span span;
    if (traceable.scope() == TraceScope.REQUIRES_NEW) {
      span = tracer.root(contextName);
    } else {
      Span parent = tracer.getCurrentSpan();
      span = parent != null ? parent.spawnChild(contextName) : tracer.root(contextName);
    }

    try (OpenSpan open = span.open()) {
      return joinPoint.proceed();
    }
  }

  /**
   * Determines the name of the span based on the {@link Traceable} annotation.
   *
   * @param joinPoint the intercepted join point
   * @param traceable the traceable annotation
   * @return the span name to use
   */
  protected String findContextName(ProceedingJoinPoint joinPoint, Traceable traceable) {
    return StringUtils.hasLength(traceable.name()) ? traceable.name() : findMethodName(joinPoint);
  }

  /**
   * Finds the {@link Traceable} annotation on the method or class.
   *
   * @param joinPoint the intercepted join point
   * @return the annotation if present, or null otherwise
   */
  protected @Nullable Traceable findAnnotation(ProceedingJoinPoint joinPoint) {
    MethodSignature sig = (MethodSignature) joinPoint.getSignature();
    Method method = sig.getMethod();

    Traceable traceable = AnnotatedElementUtils.findMergedAnnotation(method, Traceable.class);

    if (traceable == null) {
      Class<?> clazz = joinPoint.getTarget().getClass();
      traceable = AnnotatedElementUtils.findMergedAnnotation(clazz, Traceable.class);
    }
    return traceable;
  }

  /**
   * Builds a default method name for the span using the declaring class and method name.
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

package io.github.malczuuu.tracekitchen.spring.aspect;

import io.github.malczuuu.tracekitchen.OpenContext;
import io.github.malczuuu.tracekitchen.TraceContext;
import io.github.malczuuu.tracekitchen.Tracer;
import io.github.malczuuu.tracekitchen.annotation.TraceScope;
import io.github.malczuuu.tracekitchen.annotation.Traceable;
import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.jspecify.annotations.Nullable;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.StringUtils;

@Aspect
public class TracePropagatedAspect {

  private final Tracer tracer;

  public TracePropagatedAspect(Tracer tracer) {
    this.tracer = tracer;
  }

  @Around(
      "@annotation(io.github.malczuuu.tracekitchen.annotation.Traceable)"
          + "|| @within(io.github.malczuuu.tracekitchen.annotation.Traceable)")
  public Object aroundTraceable(ProceedingJoinPoint joinPoint) throws Throwable {
    Traceable traceable = findAnnotation(joinPoint);

    if (traceable == null) {
      return joinPoint.proceed();
    }

    String contextName = findContextName(joinPoint, traceable);

    TraceContext context;

    if (traceable.scope() == TraceScope.REQUIRES_NEW) {
      context = tracer.newRootContext(contextName);
    } else {
      TraceContext parent = tracer.getCurrentContext();
      context = parent != null ? parent.makeChild(contextName) : tracer.newRootContext(contextName);
    }

    try (OpenContext open = tracer.open(context)) {
      return joinPoint.proceed();
    }
  }

  private String findContextName(ProceedingJoinPoint joinPoint, Traceable traceable) {
    return StringUtils.hasLength(traceable.name()) ? traceable.name() : findMethodName(joinPoint);
  }

  private @Nullable Traceable findAnnotation(ProceedingJoinPoint joinPoint) {
    MethodSignature sig = (MethodSignature) joinPoint.getSignature();
    Method method = sig.getMethod();

    Traceable traceable = AnnotatedElementUtils.findMergedAnnotation(method, Traceable.class);

    if (traceable == null) {
      Class<?> clazz = joinPoint.getTarget().getClass();
      traceable = AnnotatedElementUtils.findMergedAnnotation(clazz, Traceable.class);
    }
    return traceable;
  }

  private String findMethodName(ProceedingJoinPoint joinPoint) {
    MethodSignature sig = (MethodSignature) joinPoint.getSignature();
    Method method = sig.getMethod();
    return method.getDeclaringClass().getSimpleName() + "." + method.getName();
  }
}

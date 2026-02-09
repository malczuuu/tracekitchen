package io.github.malczuuu.tracekitchen.spring.aspect;

import io.github.malczuuu.tracekitchen.OpenContext;
import io.github.malczuuu.tracekitchen.TraceContext;
import io.github.malczuuu.tracekitchen.Tracer;
import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class TraceScheduledAspect {

  private final Tracer tracer;

  public TraceScheduledAspect(Tracer tracer) {
    this.tracer = tracer;
  }

  @Around("@annotation(org.springframework.scheduling.annotation.Scheduled)")
  public Object aroundTraceable(ProceedingJoinPoint joinPoint) throws Throwable {

    TraceContext context = tracer.getCurrentContext();

    if (context == null) {
      context = tracer.newRootContext(findMethodName(joinPoint));
    }

    try (OpenContext open = tracer.open(context)) {
      return joinPoint.proceed();
    }
  }

  private String findMethodName(ProceedingJoinPoint joinPoint) {
    MethodSignature sig = (MethodSignature) joinPoint.getSignature();
    Method method = sig.getMethod();
    return method.getDeclaringClass().getSimpleName() + "." + method.getName();
  }
}

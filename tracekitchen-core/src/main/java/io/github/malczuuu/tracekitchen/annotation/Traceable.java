package io.github.malczuuu.tracekitchen.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class or method as traceable for distributed tracing.
 *
 * <p>When a method or class is annotated with {@code @Traceable}, a tracing aspect or interceptor
 * can automatically create and manage a {@code Span} for the duration of the method execution.
 *
 * <p>The {@link TraceScope} determines whether a new root span is created or whether an existing
 * span is reused:
 *
 * <ul>
 *   <li>{@link TraceScope#REQUIRED} – use the current span if one exists, otherwise create a new
 *       span. Nested calls create child spans.
 *   <li>{@link TraceScope#REQUIRES_NEW} – always create a new root span, ignoring any current one.
 * </ul>
 *
 * <p>This annotation can be placed on classes or methods. When placed on a class, it applies to all
 * public methods of the class unless overridden at the method level.
 *
 * @see io.github.malczuuu.tracekitchen.Span
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Traceable {

  /**
   * Defines the trace name for this method or class.
   *
   * @return the trace name
   */
  String name() default "";

  /**
   * Defines the tracing scope for this method or class.
   *
   * <p>Defaults to {@link TraceScope#REQUIRED}.
   *
   * @return the trace scope
   */
  TraceScope scope() default TraceScope.REQUIRED;
}

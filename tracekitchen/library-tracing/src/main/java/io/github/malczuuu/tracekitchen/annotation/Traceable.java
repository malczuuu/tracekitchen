package io.github.malczuuu.tracekitchen.annotation;

import io.github.malczuuu.tracekitchen.TraceContext;
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
 * can automatically create and manage a {@code TraceContext} for the duration of the method
 * execution.
 *
 * <p>The {@link TraceScope} determines whether a new root context is created or whether an existing
 * context is reused:
 *
 * <ul>
 *   <li>{@link TraceScope#REQUIRED} – use the current context if one exists, otherwise create a new
 *       context. Nested calls create child spans.
 *   <li>{@link TraceScope#REQUIRES_NEW} – always create a new root context, ignoring any current
 *       context.
 * </ul>
 *
 * <p>This annotation can be placed on classes or methods. When placed on a class, it applies to all
 * public methods of the class unless overridden at the method level.
 *
 * @see TraceContext
 * @deprecated Marked as {@code @Deprecated} tue to its experimental nature. May change in the
 *     future, so use with caution.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Deprecated
public @interface Traceable {

  /**
   * Defines the tracing scope for this method or class.
   *
   * <p>Defaults to {@link TraceScope#REQUIRED}.
   *
   * @return the trace scope
   */
  TraceScope scope() default TraceScope.REQUIRED;
}

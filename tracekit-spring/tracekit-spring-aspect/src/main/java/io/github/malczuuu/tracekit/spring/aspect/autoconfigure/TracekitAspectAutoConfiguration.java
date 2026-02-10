package io.github.malczuuu.tracekit.spring.aspect.autoconfigure;

import io.github.malczuuu.tracekit.Tracer;
import io.github.malczuuu.tracekit.spring.aspect.ScheduledTracingAspect;
import io.github.malczuuu.tracekit.spring.aspect.TraceableTracingAspect;
import org.aspectj.weaver.Advice;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/** Auto-configuration for TraceKit Aspect module. */
@AutoConfiguration
@ConditionalOnBooleanProperty(name = "tracekit.aspect.enabled", matchIfMissing = true)
@ConditionalOnClass({AopAutoConfiguration.class, Advice.class})
@EnableConfigurationProperties(TracekitAspectProperties.class)
public final class TracekitAspectAutoConfiguration {

  @Bean
  @ConditionalOnBooleanProperty(name = "tracekit.aspect.scheduled-enabled", matchIfMissing = true)
  @ConditionalOnMissingBean(ScheduledTracingAspect.class)
  ScheduledTracingAspect tracekitScheduledTracingAspect(Tracer tracer) {
    return new ScheduledTracingAspect(tracer);
  }

  @Bean
  @ConditionalOnBooleanProperty(name = "tracekit.aspect.traceable-enabled", matchIfMissing = true)
  @ConditionalOnMissingBean(TraceableTracingAspect.class)
  TraceableTracingAspect tracekitTraceableTracingAspect(Tracer tracer) {
    return new TraceableTracingAspect(tracer);
  }
}

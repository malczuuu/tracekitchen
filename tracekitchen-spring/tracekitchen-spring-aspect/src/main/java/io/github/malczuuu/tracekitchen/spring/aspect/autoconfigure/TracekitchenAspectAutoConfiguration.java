package io.github.malczuuu.tracekitchen.spring.aspect.autoconfigure;

import io.github.malczuuu.tracekitchen.Tracer;
import io.github.malczuuu.tracekitchen.spring.aspect.TracePropagatedAspect;
import io.github.malczuuu.tracekitchen.spring.aspect.TraceScheduledAspect;
import org.aspectj.weaver.Advice;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnBooleanProperty(name = "tracekitchen.aspect.enabled", matchIfMissing = true)
@ConditionalOnClass({AopAutoConfiguration.class, Advice.class})
@EnableConfigurationProperties(TracekitchenAspectProperties.class)
public final class TracekitchenAspectAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(TracePropagatedAspect.class)
  public TracePropagatedAspect tracekitchenTracePropagatedAspect(Tracer tracer) {
    return new TracePropagatedAspect(tracer);
  }

  @Bean
  @ConditionalOnMissingBean(TraceScheduledAspect.class)
  public TraceScheduledAspect tracekitchenTraceScheduledAspect(Tracer tracer) {
    return new TraceScheduledAspect(tracer);
  }
}

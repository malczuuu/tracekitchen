package io.github.malczuuu.tracekitchen.spring.aspect.autoconfigure;

import io.github.malczuuu.tracekitchen.Tracer;
import io.github.malczuuu.tracekitchen.spring.aspect.TraceableAspect;
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
  @ConditionalOnMissingBean(TraceableAspect.class)
  public TraceableAspect tracekitchenTraceableAdvisor(Tracer tracer) {
    return new TraceableAspect(tracer);
  }
}

package io.github.malczuuu.tracekit.boot4.webmvc.autoconfigure;

import io.github.malczuuu.tracekit.Tracer;
import io.github.malczuuu.tracekit.boot4.autoconfigure.TracekitProperties;
import io.github.malczuuu.tracekit.boot4.webmvc.ServletRequestExtractor;
import io.github.malczuuu.tracekit.boot4.webmvc.TracingAwareFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.webmvc.autoconfigure.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;

/** Auto-configuration for TraceKit WebMVC module. */
@AutoConfiguration
@ConditionalOnBean(Tracer.class)
@ConditionalOnBooleanProperty(name = "tracekit.webmvc.enabled", matchIfMissing = true)
@ConditionalOnClass(WebMvcAutoConfiguration.class)
@EnableConfigurationProperties(TracekitWebMvcProperties.class)
public final class TracekitWebMvcAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(TracingAwareFilter.class)
  TracingAwareFilter tracekitTracingAwareFilter(Tracer tracer, TracekitProperties properties) {
    return new TracingAwareFilter(new ServletRequestExtractor(tracer, properties), tracer);
  }
}

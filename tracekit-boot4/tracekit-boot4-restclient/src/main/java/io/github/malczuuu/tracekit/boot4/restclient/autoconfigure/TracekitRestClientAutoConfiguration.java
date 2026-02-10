package io.github.malczuuu.tracekit.boot4.restclient.autoconfigure;

import io.github.malczuuu.tracekit.Tracer;
import io.github.malczuuu.tracekit.boot4.autoconfigure.TracekitProperties;
import io.github.malczuuu.tracekit.boot4.restclient.TracingHttpRequestInterceptor;
import io.github.malczuuu.tracekit.boot4.restclient.TracingRestClientCustomizer;
import io.github.malczuuu.tracekit.boot4.restclient.TracingRestTemplateCustomizer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.restclient.autoconfigure.RestClientAutoConfiguration;
import org.springframework.context.annotation.Bean;

/** Auto-configuration for TraceKit RestClient module. */
@AutoConfiguration
@ConditionalOnBean(Tracer.class)
@ConditionalOnBooleanProperty(name = "tracekit.restclient.enabled", matchIfMissing = true)
@ConditionalOnClass(RestClientAutoConfiguration.class)
@EnableConfigurationProperties(TracekitRestClientProperties.class)
public final class TracekitRestClientAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(TracingHttpRequestInterceptor.class)
  TracingHttpRequestInterceptor tracekitTracingHttpRequestInterceptor(
      Tracer tracer, TracekitProperties properties) {
    return new TracingHttpRequestInterceptor(tracer, properties);
  }

  @Bean
  @ConditionalOnMissingBean(TracingRestClientCustomizer.class)
  TracingRestClientCustomizer tracekitTracingRestClientCustomizer(
      TracingHttpRequestInterceptor clientHttpRequestInterceptor) {
    return builder -> builder.requestInterceptor(clientHttpRequestInterceptor);
  }

  @Bean
  @ConditionalOnMissingBean(TracingRestTemplateCustomizer.class)
  public TracingRestTemplateCustomizer tracekitTracingRestTemplateCustomizer(
      TracingHttpRequestInterceptor clientHttpRequestInterceptor) {
    return builder -> builder.getInterceptors().add(clientHttpRequestInterceptor);
  }
}

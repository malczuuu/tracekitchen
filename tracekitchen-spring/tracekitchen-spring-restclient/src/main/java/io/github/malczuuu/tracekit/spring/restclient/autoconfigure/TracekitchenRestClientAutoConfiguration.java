package io.github.malczuuu.tracekit.spring.restclient.autoconfigure;

import io.github.malczuuu.tracekit.Tracer;
import io.github.malczuuu.tracekit.spring.autoconfigure.TracekitchenProperties;
import io.github.malczuuu.tracekit.spring.restclient.TracingHttpRequestInterceptor;
import io.github.malczuuu.tracekit.spring.restclient.TracingRestClientCustomizer;
import io.github.malczuuu.tracekit.spring.restclient.TracingRestTemplateCustomizer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.restclient.autoconfigure.RestClientAutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnBooleanProperty(name = "tracekitchen.restclient.enabled", matchIfMissing = true)
@ConditionalOnClass(RestClientAutoConfiguration.class)
@EnableConfigurationProperties(TracekitchenRestClientProperties.class)
public final class TracekitchenRestClientAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(TracingHttpRequestInterceptor.class)
  TracingHttpRequestInterceptor tracekitchenTracingHttpRequestInterceptor(
      Tracer tracer, TracekitchenProperties properties) {
    return new TracingHttpRequestInterceptor(tracer, properties);
  }

  @Bean
  @ConditionalOnMissingBean(TracingRestClientCustomizer.class)
  TracingRestClientCustomizer tracekitchenTracingRestClientCustomizer(
      TracingHttpRequestInterceptor clientHttpRequestInterceptor) {
    return builder -> builder.requestInterceptor(clientHttpRequestInterceptor);
  }

  @Bean
  @ConditionalOnMissingBean(TracingRestTemplateCustomizer.class)
  public TracingRestTemplateCustomizer tracekitchenTracingRestTemplateCustomizer(
      TracingHttpRequestInterceptor clientHttpRequestInterceptor) {
    return builder -> builder.getInterceptors().add(clientHttpRequestInterceptor);
  }
}

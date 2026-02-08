package io.github.malczuuu.tracekitchen.spring.restclient.autoconfigure;

import io.github.malczuuu.tracekitchen.Tracer;
import io.github.malczuuu.tracekitchen.spring.autoconfigure.TracekitchenProperties;
import io.github.malczuuu.tracekitchen.spring.restclient.TracingHttpRequestInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(TracekitchenRestClientProperties.class)
public final class TracekitchenRestClientAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(TracingHttpRequestInterceptor.class)
  TracingHttpRequestInterceptor tracekitchenTracingHttpRequestInterceptor(
      Tracer tracer, TracekitchenProperties properties) {
    return new TracingHttpRequestInterceptor(tracer, properties);
  }
}

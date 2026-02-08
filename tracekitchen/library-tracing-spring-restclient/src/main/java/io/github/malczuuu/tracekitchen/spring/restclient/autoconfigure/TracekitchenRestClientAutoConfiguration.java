package io.github.malczuuu.tracekitchen.spring.restclient.autoconfigure;

import io.github.malczuuu.tracekitchen.Tracer;
import io.github.malczuuu.tracekitchen.spring.autoconfigure.TracekitchenProperties;
import io.github.malczuuu.tracekitchen.spring.restclient.TracingHttpRequestInterceptor;
import io.github.malczuuu.tracekitchen.spring.restclient.TracingRestClientCustomizer;
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
  @ConditionalOnMissingBean(TracingRestClientCustomizer.class)
  TracingRestClientCustomizer tracekitchenTracingRestClientCustomizer(
      Tracer tracer, TracekitchenProperties properties) {
    return builder ->
        builder.requestInterceptor(new TracingHttpRequestInterceptor(tracer, properties));
  }
}

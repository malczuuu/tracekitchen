package io.github.malczuuu.tracekitchen.tracing.webmvc.autoconfigure;

import io.github.malczuuu.tracekitchen.tracing.core.SimpleTracerBuilder;
import io.github.malczuuu.tracekitchen.tracing.core.Tracer;
import io.github.malczuuu.tracekitchen.tracing.webmvc.DefaultServletRequestExtractor;
import io.github.malczuuu.tracekitchen.tracing.webmvc.DefaultTracingHttpRequestInterceptor;
import io.github.malczuuu.tracekitchen.tracing.webmvc.ServletRequestExtractor;
import io.github.malczuuu.tracekitchen.tracing.webmvc.SimpleTracerBuilderCustomizer;
import io.github.malczuuu.tracekitchen.tracing.webmvc.TracingHttpRequestInterceptor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(TracingWebMvcProperties.class)
public final class TracingWebMvcAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(SimpleTracerBuilder.class)
  SimpleTracerBuilder simpleTracerBuilder(ObjectProvider<SimpleTracerBuilderCustomizer> provider) {
    SimpleTracerBuilder builder = new SimpleTracerBuilder();
    for (SimpleTracerBuilderCustomizer customizer : provider.orderedStream().toList()) {
      builder = customizer.customize(builder);
    }
    return builder;
  }

  @Bean
  @ConditionalOnMissingBean(Tracer.class)
  Tracer tracer(SimpleTracerBuilder simpleTracerBuilder) {
    return simpleTracerBuilder.build();
  }

  @Bean
  @ConditionalOnMissingBean(TracingHttpRequestInterceptor.class)
  TracingHttpRequestInterceptor tracingHttpRequestInterceptor(
      Tracer tracer, TracingWebMvcProperties properties) {
    return new DefaultTracingHttpRequestInterceptor(tracer, properties);
  }

  @Bean
  @ConditionalOnMissingBean(ServletRequestExtractor.class)
  ServletRequestExtractor servletRequestExtractor(
      Tracer tracer, TracingWebMvcProperties properties) {
    return new DefaultServletRequestExtractor(tracer, properties);
  }
}

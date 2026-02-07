package io.github.malczuuu.tracekitchen.tracing.webmvc.autoconfigure;

import io.github.malczuuu.tracekitchen.tracing.core.LoggingContextAdapter;
import io.github.malczuuu.tracekitchen.tracing.core.SimpleTracer;
import io.github.malczuuu.tracekitchen.tracing.core.TraceFactory;
import io.github.malczuuu.tracekitchen.tracing.core.Tracer;
import io.github.malczuuu.tracekitchen.tracing.webmvc.DefaultLoggingContextAdapter;
import io.github.malczuuu.tracekitchen.tracing.webmvc.DefaultServletRequestExtractor;
import io.github.malczuuu.tracekitchen.tracing.webmvc.DefaultTracingHttpRequestInterceptor;
import io.github.malczuuu.tracekitchen.tracing.webmvc.ServletRequestExtractor;
import io.github.malczuuu.tracekitchen.tracing.webmvc.TracingHttpRequestInterceptor;
import java.time.Clock;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(TracingWebMvcProperties.class)
public final class TracingWebMvcAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(Clock.class)
  Clock clock() {
    return Clock.systemUTC();
  }

  @Bean
  @ConditionalOnMissingBean(TraceFactory.class)
  TraceFactory traceFactory() {
    return TraceFactory.getInstance();
  }

  @Bean
  @ConditionalOnMissingBean(LoggingContextAdapter.class)
  LoggingContextAdapter loggingContextAdapter() {
    return new DefaultLoggingContextAdapter();
  }

  @Bean
  @ConditionalOnMissingBean(Tracer.class)
  Tracer tracer(
      TraceFactory traceFactory, LoggingContextAdapter loggingContextAdapter, Clock clock) {
    return new SimpleTracer(traceFactory, loggingContextAdapter, clock);
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

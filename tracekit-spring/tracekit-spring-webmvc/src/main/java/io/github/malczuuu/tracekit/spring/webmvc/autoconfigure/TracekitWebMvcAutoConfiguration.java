package io.github.malczuuu.tracekit.spring.webmvc.autoconfigure;

import io.github.malczuuu.tracekit.Tracer;
import io.github.malczuuu.tracekit.spring.autoconfigure.TracekitProperties;
import io.github.malczuuu.tracekit.spring.webmvc.ServletRequestExtractor;
import io.github.malczuuu.tracekit.spring.webmvc.TraceExtractorFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.webmvc.autoconfigure.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnBooleanProperty(name = "tracekitchen.webmvc.enabled", matchIfMissing = true)
@ConditionalOnClass(WebMvcAutoConfiguration.class)
@EnableConfigurationProperties(TracekitWebMvcProperties.class)
public class TracekitWebMvcAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(ServletRequestExtractor.class)
  ServletRequestExtractor tracekitchenServletRequestExtractor(
      Tracer tracer, TracekitProperties properties) {
    return new ServletRequestExtractor(tracer, properties);
  }

  @Bean
  @ConditionalOnMissingBean(TraceExtractorFilter.class)
  TraceExtractorFilter tracekitchenTraceExtractorFilter(
      ServletRequestExtractor servletRequestExtractor, Tracer tracer) {
    return new TraceExtractorFilter(servletRequestExtractor, tracer);
  }
}

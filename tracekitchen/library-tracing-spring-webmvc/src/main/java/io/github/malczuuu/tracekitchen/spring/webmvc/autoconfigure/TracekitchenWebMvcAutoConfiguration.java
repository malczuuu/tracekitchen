package io.github.malczuuu.tracekitchen.spring.webmvc.autoconfigure;

import io.github.malczuuu.tracekitchen.Tracer;
import io.github.malczuuu.tracekitchen.spring.autoconfigure.TracekitchenProperties;
import io.github.malczuuu.tracekitchen.spring.webmvc.ServletRequestExtractor;
import io.github.malczuuu.tracekitchen.spring.webmvc.TraceExtractorFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(TracekitchenWebMvcProperties.class)
public class TracekitchenWebMvcAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(ServletRequestExtractor.class)
  ServletRequestExtractor tracekitchenServletRequestExtractor(
      Tracer tracer, TracekitchenProperties properties) {
    return new ServletRequestExtractor(tracer, properties);
  }

  @Bean
  @ConditionalOnMissingBean(TraceExtractorFilter.class)
  TraceExtractorFilter tracekitchenTraceExtractorFilter(
      ServletRequestExtractor servletRequestExtractor, Tracer tracer) {
    return new TraceExtractorFilter(servletRequestExtractor, tracer);
  }
}

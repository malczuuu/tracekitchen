package io.github.malczuuu.tracekitchen.spring.webmvc.autoconfigure;

import io.github.malczuuu.tracekitchen.Tracer;
import io.github.malczuuu.tracekitchen.spring.autoconfigure.TracekitchenProperties;
import io.github.malczuuu.tracekitchen.spring.webmvc.DefaultServletRequestExtractor;
import io.github.malczuuu.tracekitchen.spring.webmvc.ServletRequestExtractor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(TracekitchenWebMvcProperties.class)
public final class TracekitchenWebMvcConfiguration {

  @Bean
  @ConditionalOnMissingBean(ServletRequestExtractor.class)
  ServletRequestExtractor servletRequestExtractor(
      Tracer tracer, TracekitchenProperties properties) {
    return new DefaultServletRequestExtractor(tracer, properties);
  }
}

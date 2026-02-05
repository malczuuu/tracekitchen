package io.github.malczuuu.tracekitchen.common.autoconfigure;

import io.github.malczuuu.tracekitchen.common.logging.LoggingAdviceInspector;
import io.github.malczuuu.tracekitchen.common.logging.LoggingFilter;
import io.github.malczuuu.tracekitchen.common.support.OpenApiPathSorter;
import java.time.Clock;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@AutoConfiguration
@EnableConfigurationProperties(CommonProperties.class)
public final class CommonAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(Clock.class)
  Clock clock() {
    return Clock.systemDefaultZone();
  }

  @Bean
  @ConditionalOnMissingBean(LoggingFilter.class)
  LoggingFilter loggingFilter(Clock clock) {
    return new LoggingFilter(clock);
  }

  @Bean
  @ConditionalOnMissingBean(LoggingAdviceInspector.class)
  public LoggingAdviceInspector loggingAdviceInspector() {
    return new LoggingAdviceInspector();
  }

  @Bean
  @ConditionalOnMissingBean(RestClient.class)
  public RestClient restClient(RestClient.Builder builder) {
    return builder.build();
  }

  @Bean
  @ConditionalOnMissingBean(OpenApiPathSorter.class)
  OpenApiPathSorter openApiPathSorter(CommonProperties properties) {
    return new OpenApiPathSorter(properties.getOpenapiTitle());
  }
}

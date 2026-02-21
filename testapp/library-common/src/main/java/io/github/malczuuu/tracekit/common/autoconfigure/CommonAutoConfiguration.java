package io.github.malczuuu.tracekit.common.autoconfigure;

import io.github.malczuuu.tracekit.common.LoggingAdviceInspector;
import io.github.malczuuu.tracekit.common.LoggingFilter;
import io.github.malczuuu.tracekit.common.OpenApiPathSorter;
import java.time.Clock;
import java.time.ZoneId;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestClient;

@AutoConfiguration
@EnableConfigurationProperties(CommonProperties.class)
@Import(TaskExecutorConfiguration.class)
public final class CommonAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(Clock.class)
  Clock clock() {
    return Clock.system(ZoneId.systemDefault());
  }

  @Bean
  @ConditionalOnMissingBean(LoggingFilter.class)
  LoggingFilter loggingFilter(Clock clock) {
    return new LoggingFilter(clock);
  }

  @Bean
  @ConditionalOnMissingBean(LoggingAdviceInspector.class)
  LoggingAdviceInspector loggingAdviceInspector() {
    return new LoggingAdviceInspector();
  }

  @Bean
  @ConditionalOnMissingBean(RestClient.class)
  RestClient restClient(RestClient.Builder builder) {
    return builder.build();
  }

  @Bean
  @ConditionalOnMissingBean(OpenApiPathSorter.class)
  OpenApiPathSorter openApiPathSorter(CommonProperties properties) {
    return new OpenApiPathSorter(properties.getOpenapiTitle());
  }
}

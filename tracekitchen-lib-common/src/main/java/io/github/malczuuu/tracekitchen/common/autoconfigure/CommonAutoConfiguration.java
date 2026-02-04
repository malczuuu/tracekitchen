package io.github.malczuuu.tracekitchen.common.autoconfigure;

import io.github.malczuuu.tracekitchen.common.LoggingAdviceInspector;
import io.github.malczuuu.tracekitchen.common.LoggingFilter;
import java.time.Clock;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@AutoConfiguration
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
}

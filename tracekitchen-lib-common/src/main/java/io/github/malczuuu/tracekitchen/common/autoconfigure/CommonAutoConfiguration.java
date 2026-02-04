package io.github.malczuuu.tracekitchen.common.autoconfigure;

import io.github.malczuuu.tracekitchen.common.LoggingFilter;
import java.time.Clock;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

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
}

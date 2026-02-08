package io.github.malczuuu.tracekitchen.spring.autoconfigure;

import io.github.malczuuu.tracekitchen.SimpleTracerBuilder;
import io.github.malczuuu.tracekitchen.Tracer;
import io.github.malczuuu.tracekitchen.spring.LoggingContextLifecycleAdapter;
import io.github.malczuuu.tracekitchen.spring.LoggingContextSimpleTracerBuilderCustomizer;
import io.github.malczuuu.tracekitchen.spring.SimpleTracerBuilderCustomizer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(TracekitchenProperties.class)
public class TracekitchenAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(LoggingContextSimpleTracerBuilderCustomizer.class)
  LoggingContextSimpleTracerBuilderCustomizer tracekitchenLoggingSimpleTracerBuilderCustomizer() {
    return builder -> builder.addLifecycleAdapter(new LoggingContextLifecycleAdapter());
  }

  @Bean
  @ConditionalOnMissingBean(SimpleTracerBuilder.class)
  SimpleTracerBuilder tracekitchenSimpleTracerBuilder(
      ObjectProvider<SimpleTracerBuilderCustomizer> provider) {
    SimpleTracerBuilder builder = new SimpleTracerBuilder();
    for (SimpleTracerBuilderCustomizer customizer : provider.orderedStream().toList()) {
      builder = customizer.customize(builder);
    }
    return builder;
  }

  @Bean
  @ConditionalOnMissingBean(Tracer.class)
  Tracer tracekitchenTracer(SimpleTracerBuilder simpleTracerBuilder) {
    return simpleTracerBuilder.build();
  }
}

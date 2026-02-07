package io.github.malczuuu.tracekitchen.spring.autoconfigure;

import io.github.malczuuu.tracekitchen.SimpleTracerBuilder;
import io.github.malczuuu.tracekitchen.Tracer;
import io.github.malczuuu.tracekitchen.spring.SimpleTracerBuilderCustomizer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(TracekitchenProperties.class)
public final class TracekitchenAutoConfiguration {

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
}

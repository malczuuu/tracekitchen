package io.github.malczuuu.tracekit.spring.autoconfigure;

import io.github.malczuuu.tracekit.Tracer;
import io.github.malczuuu.tracekit.simple.SimpleTracerBuilder;
import io.github.malczuuu.tracekit.spring.LoggingContextLifecycleAdapter;
import io.github.malczuuu.tracekit.spring.LoggingContextSimpleTracerBuilderCustomizer;
import io.github.malczuuu.tracekit.spring.SimpleTracerBuilderCustomizer;
import io.github.malczuuu.tracekit.spring.TracingTaskDecorator;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/** Auto-configuration for TraceKit module. */
@AutoConfiguration
@ConditionalOnBooleanProperty(name = "tracekit.enabled", matchIfMissing = true)
@EnableConfigurationProperties(TracekitProperties.class)
public final class TracekitAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(LoggingContextSimpleTracerBuilderCustomizer.class)
  LoggingContextSimpleTracerBuilderCustomizer tracekitLoggingSimpleTracerBuilderCustomizer() {
    return builder -> builder.addLifecycleAdapter(new LoggingContextLifecycleAdapter());
  }

  @Bean
  @ConditionalOnMissingBean(SimpleTracerBuilder.class)
  SimpleTracerBuilder tracekitSimpleTracerBuilder(
      ObjectProvider<SimpleTracerBuilderCustomizer> provider) {
    SimpleTracerBuilder builder = new SimpleTracerBuilder();
    for (SimpleTracerBuilderCustomizer customizer : provider.orderedStream().toList()) {
      builder = customizer.customize(builder);
    }
    return builder;
  }

  @Bean
  @ConditionalOnMissingBean(Tracer.class)
  Tracer tracekitTracer(SimpleTracerBuilder simpleTracerBuilder) {
    return simpleTracerBuilder.build();
  }

  @Bean
  @ConditionalOnMissingBean(TracingTaskDecorator.class)
  TracingTaskDecorator tracekitTracingTaskDecorator(Tracer tracer) {
    return new TracingTaskDecorator(tracer);
  }
}

/*
 * Copyright (c) 2026 Damian Malczewski
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.github.malczuuu.tracekit.boot4.autoconfigure;

import io.github.malczuuu.tracekit.Tracer;
import io.github.malczuuu.tracekit.boot4.LoggingContextLifecycleAdapter;
import io.github.malczuuu.tracekit.boot4.LoggingContextSimpleTracerBuilderCustomizer;
import io.github.malczuuu.tracekit.boot4.SimpleTracerBuilderCustomizer;
import io.github.malczuuu.tracekit.boot4.TracingTaskDecorator;
import io.github.malczuuu.tracekit.simple.SimpleTracerBuilder;
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

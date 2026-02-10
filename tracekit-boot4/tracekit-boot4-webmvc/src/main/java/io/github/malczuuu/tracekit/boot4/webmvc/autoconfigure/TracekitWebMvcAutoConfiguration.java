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
package io.github.malczuuu.tracekit.boot4.webmvc.autoconfigure;

import io.github.malczuuu.tracekit.Tracer;
import io.github.malczuuu.tracekit.boot4.autoconfigure.TracekitProperties;
import io.github.malczuuu.tracekit.boot4.webmvc.ServletRequestExtractor;
import io.github.malczuuu.tracekit.boot4.webmvc.TracingAwareFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.webmvc.autoconfigure.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;

/** Auto-configuration for TraceKit WebMVC module. */
@AutoConfiguration
@ConditionalOnBean(Tracer.class)
@ConditionalOnBooleanProperty(name = "tracekit.webmvc.enabled", matchIfMissing = true)
@ConditionalOnClass(WebMvcAutoConfiguration.class)
@EnableConfigurationProperties(TracekitWebMvcProperties.class)
public final class TracekitWebMvcAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(TracingAwareFilter.class)
  TracingAwareFilter tracekitTracingAwareFilter(Tracer tracer, TracekitProperties properties) {
    return new TracingAwareFilter(new ServletRequestExtractor(tracer, properties), tracer);
  }
}

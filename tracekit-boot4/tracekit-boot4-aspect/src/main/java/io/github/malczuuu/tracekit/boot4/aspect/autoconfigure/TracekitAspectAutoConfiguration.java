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

package io.github.malczuuu.tracekit.boot4.aspect.autoconfigure;

import io.github.malczuuu.tracekit.Tracer;
import io.github.malczuuu.tracekit.boot4.aspect.ScheduledTracingAspect;
import io.github.malczuuu.tracekit.boot4.aspect.TraceableTracingAspect;
import io.github.malczuuu.tracekit.boot4.autoconfigure.TracekitAutoConfiguration;
import org.aspectj.weaver.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/** Auto-configuration for TraceKit Aspect module. */
@AutoConfiguration(after = TracekitAutoConfiguration.class)
@ConditionalOnBean(Tracer.class)
@ConditionalOnBooleanProperty(name = "tracekit.aspect.enabled", matchIfMissing = true)
@ConditionalOnClass({AopAutoConfiguration.class, Advice.class})
@EnableConfigurationProperties(TracekitAspectProperties.class)
public final class TracekitAspectAutoConfiguration {

  private static final Logger log = LoggerFactory.getLogger(TracekitAspectAutoConfiguration.class);

  /**
   * Creates a new instance of {@link TracekitAspectAutoConfiguration} and logs the registration of
   * the TraceKit Aspect auto-configuration for diagnostics.
   */
  public TracekitAspectAutoConfiguration() {
    log.info("Registered TraceKit Aspect - {}", TracekitAutoConfiguration.class.getSimpleName());
  }

  @Bean
  @ConditionalOnBooleanProperty(name = "tracekit.aspect.scheduled-enabled", matchIfMissing = true)
  @ConditionalOnMissingBean(ScheduledTracingAspect.class)
  ScheduledTracingAspect tracekitScheduledTracingAspect(Tracer tracer) {
    return new ScheduledTracingAspect(tracer);
  }

  @Bean
  @ConditionalOnBooleanProperty(name = "tracekit.aspect.traceable-enabled", matchIfMissing = true)
  @ConditionalOnMissingBean(TraceableTracingAspect.class)
  TraceableTracingAspect tracekitTraceableTracingAspect(Tracer tracer) {
    return new TraceableTracingAspect(tracer);
  }
}

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

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/** Configuration properties for TraceKit Aspect module. */
@ConfigurationProperties(prefix = "tracekit.aspect")
public class TracekitAspectProperties {

  private final boolean enabled;
  private final boolean scheduledEnabled;
  private final boolean traceableEnabled;

  /**
   * Creates a new {@link TracekitAspectProperties}.
   *
   * @param enabled whether TraceKit Aspect module should be enabled; if {@code null}, defaults to
   *     {@code true}
   * @param scheduledEnabled whether {@code ScheduledTracingAspect} is enabled
   * @param traceableEnabled whether {@code TraceableTracingAspect} is enabled
   */
  public TracekitAspectProperties(
      @DefaultValue("true") boolean enabled,
      @DefaultValue("true") boolean scheduledEnabled,
      @DefaultValue("true") boolean traceableEnabled) {
    this.enabled = enabled;
    this.scheduledEnabled = scheduledEnabled;
    this.traceableEnabled = traceableEnabled;
  }

  /**
   * Returns whether TraceKit Aspect module is enabled.
   *
   * @return {@code true} if module enabled, {@code false} otherwise
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Returns whether {@code ScheduledTracingAspect} should be created.
   *
   * @return {@code true} if {@code ScheduledTracingAspect} should be created, {@code false}
   *     otherwise
   */
  public boolean isScheduledEnabled() {
    return scheduledEnabled;
  }

  /**
   * Returns whether {@code TraceableTracingAspect} should be created.
   *
   * @return {@code true} if {@code TraceableTracingAspect} should be created, {@code false}
   *     otherwise
   */
  public boolean isTraceableEnabled() {
    return traceableEnabled;
  }
}

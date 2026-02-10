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

import io.github.malczuuu.tracekit.boot4.TraceHeaderSettings;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/** Configuration properties for TraceKit Core module. */
@ConfigurationProperties(prefix = "tracekit")
public class TracekitProperties implements TraceHeaderSettings {

  private final boolean enabled;
  private final String traceIdHeaderNames;
  private final String spanIdHeaderNames;
  private final String parentSpanIdHeaderNames;

  /**
   * Creates a new {@link TracekitProperties} instance with the given header names.
   *
   * @param enabled whether TraceKit module should be enabled; if {@code null}, defaults to {@code
   *     true}
   * @param traceIdHeaderNames comma-separated list of trace ID header names
   * @param spanIdHeaderNames comma-separated list of span ID header names
   * @param parentSpanIdHeaderNames comma-separated list of parent span ID header names
   */
  public TracekitProperties(
      @DefaultValue("true") boolean enabled,
      @DefaultValue("") String traceIdHeaderNames,
      @DefaultValue("") String spanIdHeaderNames,
      @DefaultValue("") String parentSpanIdHeaderNames) {
    this.enabled = enabled;
    this.traceIdHeaderNames = traceIdHeaderNames;
    this.spanIdHeaderNames = spanIdHeaderNames;
    this.parentSpanIdHeaderNames = parentSpanIdHeaderNames;
  }

  /**
   * Returns whether TraceKit module is enabled.
   *
   * @return {@code true} if module enabled, {@code false} otherwise
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Returns the list of configured HTTP header names used to propagate the trace ID.
   *
   * @return a list of trace ID header names, empty if none are configured
   */
  @Override
  public List<String> getTraceIdHeaderNames() {
    return Stream.of(traceIdHeaderNames.split(","))
        .map(String::strip)
        .filter(s -> !s.isBlank())
        .toList();
  }

  /**
   * Returns the list of configured HTTP header names used to propagate the current span ID.
   *
   * @return a list of span ID header names, empty if none are configured
   */
  @Override
  public List<String> getSpanIdHeaderNames() {
    return Stream.of(spanIdHeaderNames.split(","))
        .map(String::strip)
        .filter(s -> !s.isBlank())
        .toList();
  }

  /**
   * Returns the list of configured HTTP header names used to propagate the parent span ID.
   *
   * @return a list of parent span ID header names, empty if none are configured
   */
  @Override
  public List<String> getParentIdHeaderNames() {
    return Stream.of(parentSpanIdHeaderNames.split(","))
        .map(String::strip)
        .filter(s -> !s.isBlank())
        .toList();
  }
}

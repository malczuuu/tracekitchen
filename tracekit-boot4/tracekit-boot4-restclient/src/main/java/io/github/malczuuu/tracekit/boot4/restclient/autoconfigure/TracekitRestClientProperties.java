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
package io.github.malczuuu.tracekit.boot4.restclient.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/** Configuration properties for the Tracekit RestClient module. */
@ConfigurationProperties(prefix = "tracekit.restclient")
public class TracekitRestClientProperties {

  private final boolean enabled;

  /**
   * Creates a new {@link TracekitRestClientProperties} instance with the given header names.
   *
   * @param enabled whether Tracekit RestClient module should be enabled; if {@code null}, defaults
   *     to {@code true}
   */
  public TracekitRestClientProperties(@DefaultValue("true") boolean enabled) {
    this.enabled = enabled;
  }

  /**
   * Returns whether Tracekit RestClient module is enabled.
   *
   * @return {@code true} if module enabled, {@code false} otherwise
   */
  public boolean isEnabled() {
    return enabled;
  }
}

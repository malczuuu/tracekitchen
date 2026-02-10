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
package io.github.malczuuu.tracekit.simple;

import io.github.malczuuu.tracekit.Trace;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

final class SimpleTrace implements Trace {

  private final String traceId;
  private final String spanId;
  private final @Nullable String parentSpanId;
  private final boolean sampled;

  SimpleTrace(String traceId, String spanId) {
    this(traceId, spanId, null, false);
  }

  SimpleTrace(String traceId, String spanId, @Nullable String parentSpanId, boolean sampled) {
    this.traceId = traceId;
    this.spanId = spanId;
    this.parentSpanId = parentSpanId;
    this.sampled = sampled;
  }

  @Override
  public String getTraceId() {
    return traceId;
  }

  @Override
  public String getSpanId() {
    return spanId;
  }

  @Override
  public @Nullable String getParentSpanId() {
    return parentSpanId;
  }

  @Override
  public boolean isSampled() {
    return sampled;
  }

  @Override
  public boolean isParentOf(@Nullable Trace trace) {
    if (trace == null) {
      return false;
    }
    return Objects.equals(traceId, trace.getTraceId())
        && Objects.equals(spanId, trace.getParentSpanId());
  }

  @Override
  public boolean isChildOf(@Nullable Trace trace) {
    if (trace == null) {
      return false;
    }
    return Objects.equals(traceId, trace.getTraceId())
        && Objects.equals(parentSpanId, trace.getSpanId());
  }

  @Override
  public Trace spawnChild(String spanId) {
    return new SimpleTrace(traceId, spanId, this.spanId, sampled);
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Trace that)) {
      return false;
    }
    return Objects.equals(traceId, that.getTraceId())
        && Objects.equals(spanId, that.getSpanId())
        && Objects.equals(parentSpanId, that.getParentSpanId())
        && sampled == that.isSampled();
  }

  @Override
  public int hashCode() {
    return Objects.hash(traceId, spanId, parentSpanId, sampled);
  }

  @Override
  public String toString() {
    List<String> lines = new ArrayList<>();
    lines.add("traceId='" + traceId + "'");
    lines.add("spanId='" + spanId + "'");
    if (parentSpanId != null) {
      lines.add("parentSpanId='" + parentSpanId + "'");
    }
    lines.add("sampled=" + sampled);

    return "[" + String.join(", ", lines) + "]";
  }
}

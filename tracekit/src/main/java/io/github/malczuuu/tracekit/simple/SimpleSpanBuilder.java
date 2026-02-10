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

import static java.util.Objects.requireNonNull;

import io.github.malczuuu.tracekit.Span;
import io.github.malczuuu.tracekit.SpanBuilder;
import io.github.malczuuu.tracekit.SpanLifecycleAdapter;
import io.github.malczuuu.tracekit.TraceFactory;
import java.time.Clock;
import org.jspecify.annotations.Nullable;

final class SimpleSpanBuilder implements SpanBuilder {

  private final @Nullable String name;
  private final @Nullable String traceId;
  private final @Nullable String spanId;
  private final @Nullable String parentSpanId;
  private final boolean sampled;

  private final Clock clock;
  private final SpanLifecycleAdapter lifecycleAdapter;
  private final TraceFactory traceFactory;

  SimpleSpanBuilder(Clock clock, SpanLifecycleAdapter lifecycleAdapter, TraceFactory traceFactory) {
    this(null, null, null, null, false, clock, lifecycleAdapter, traceFactory);
  }

  SimpleSpanBuilder(
      @Nullable String name,
      @Nullable String traceId,
      @Nullable String spanId,
      @Nullable String parentSpanId,
      boolean sampled,
      Clock clock,
      SpanLifecycleAdapter lifecycleAdapter,
      TraceFactory traceFactory) {
    this.name = name;
    this.traceId = traceId;
    this.spanId = spanId;
    this.parentSpanId = parentSpanId;
    this.sampled = sampled;
    this.clock = clock;
    this.lifecycleAdapter = lifecycleAdapter;
    this.traceFactory = traceFactory;
  }

  @Override
  public SpanBuilder withName(String name) {
    return new SimpleSpanBuilder(
        name, traceId, spanId, parentSpanId, sampled, clock, lifecycleAdapter, traceFactory);
  }

  @Override
  public SimpleSpanBuilder withTraceId(String traceId) {
    return new SimpleSpanBuilder(
        name, traceId, spanId, parentSpanId, sampled, clock, lifecycleAdapter, traceFactory);
  }

  @Override
  public SimpleSpanBuilder withSpanId(String spanId) {
    return new SimpleSpanBuilder(
        name, traceId, spanId, parentSpanId, sampled, clock, lifecycleAdapter, traceFactory);
  }

  @Override
  public SimpleSpanBuilder withParentSpanId(String parentSpanId) {
    return new SimpleSpanBuilder(
        name, traceId, spanId, parentSpanId, sampled, clock, lifecycleAdapter, traceFactory);
  }

  @Override
  public SimpleSpanBuilder withSampled(boolean sampled) {
    return new SimpleSpanBuilder(
        name, traceId, spanId, parentSpanId, sampled, clock, lifecycleAdapter, traceFactory);
  }

  @Override
  public boolean isComplete() {
    return hasText(traceId);
  }

  @Override
  public Span build() {
    return new SimpleSpan(name, buildTrace(), clock, lifecycleAdapter, traceFactory);
  }

  private SimpleTrace buildTrace() {
    String traceId = isTraceIdValid() ? this.traceId : traceFactory.makeTraceId();
    String spanId = isSpanIdValid() ? this.spanId : traceFactory.makeSpanId();
    String parentSpanId = isSpanIdValid() ? this.parentSpanId : null;

    return new SimpleTrace(requireNonNull(traceId), requireNonNull(spanId), parentSpanId, sampled);
  }

  private boolean isTraceIdValid() {
    return hasText(traceId);
  }

  private boolean isSpanIdValid() {
    return hasText(traceId) && hasText(spanId);
  }

  private static boolean hasText(@Nullable String str) {
    return str != null && !str.isBlank();
  }
}

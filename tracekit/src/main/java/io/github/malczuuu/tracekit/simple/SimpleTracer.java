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

import io.github.malczuuu.tracekit.Span;
import io.github.malczuuu.tracekit.SpanBuilder;
import io.github.malczuuu.tracekit.SpanLifecycleAdapter;
import io.github.malczuuu.tracekit.TraceFactory;
import io.github.malczuuu.tracekit.Tracer;
import java.time.Clock;
import org.jspecify.annotations.Nullable;

/** Simple implementation of the {@link Tracer} interface. */
public class SimpleTracer implements Tracer {

  /**
   * Creates {@link SimpleTracer} instance with default configuration.
   *
   * @return new default instance
   */
  public static SimpleTracer create() {
    return new SimpleTracerBuilder().build();
  }

  private final TraceFactory traceFactory;
  private final SpanLifecycleAdapter lifecycleAdapter;
  private final Clock clock;

  SimpleTracer(TraceFactory traceFactory, SpanLifecycleAdapter lifecycleAdapter, Clock clock) {
    this.traceFactory = traceFactory;
    this.lifecycleAdapter = lifecycleAdapter;
    this.clock = clock;
  }

  /**
   * {@inheritDoc}
   *
   * @return a new {@link Span} representing the root span
   */
  @Override
  public Span root() {
    return new SimpleSpan(clock, lifecycleAdapter, traceFactory);
  }

  /**
   * {@inheritDoc}
   *
   * @param name the name of the child span; must not be {@code null} or blank
   * @return a new {@link Span} representing the root span
   */
  @Override
  public Span root(String name) {
    return new SimpleSpan(name, clock, lifecycleAdapter, traceFactory);
  }

  /**
   * {@inheritDoc}
   *
   * @return a new {@link SpanBuilder}
   */
  @Override
  public SpanBuilder builder() {
    return new SimpleSpanBuilder(clock, lifecycleAdapter, traceFactory);
  }

  /**
   * {@inheritDoc}
   *
   * @return the currently active {@link Span}, or {@code null} if none
   */
  @Override
  public @Nullable Span getCurrentSpan() {
    return ThreadLocalSpanHolder.current();
  }
}

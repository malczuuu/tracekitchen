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

import io.github.malczuuu.tracekit.SpanLifecycleAdapter;
import io.github.malczuuu.tracekit.TraceFactory;
import java.time.Clock;
import org.jspecify.annotations.Nullable;

/** Builder for creating instances of {@link SimpleTracer}. */
public class SimpleTracerBuilder {

  private final @Nullable TraceFactory traceFactory;
  private final CompositeLifecycleAdapter lifecycleAdapter;
  private final @Nullable Clock clock;

  /** Creates new {@link SimpleTracerBuilder} object. */
  public SimpleTracerBuilder() {
    this(null, CompositeLifecycleAdapter.empty(), null);
  }

  private SimpleTracerBuilder(
      @Nullable TraceFactory traceFactory,
      CompositeLifecycleAdapter lifecycleAdapter,
      @Nullable Clock clock) {
    this.traceFactory = traceFactory;
    this.lifecycleAdapter = lifecycleAdapter;
    this.clock = clock;
  }

  /**
   * Sets the {@link TraceFactory} to be used by the tracer.
   *
   * @param traceFactory the {@link TraceFactory} to use, or {@code null} to use the default
   * @return a new builder instance with the given trace factory
   */
  public SimpleTracerBuilder withTraceFactory(@Nullable TraceFactory traceFactory) {
    return new SimpleTracerBuilder(traceFactory, lifecycleAdapter, clock);
  }

  /**
   * Adds the {@link SpanLifecycleAdapter} to receive span lifecycle events.
   *
   * @param lifecycleAdapter the lifecycle adapter to add
   * @return a new builder instance with the given span lifecycle adapter
   */
  public SimpleTracerBuilder addLifecycleAdapter(@Nullable SpanLifecycleAdapter lifecycleAdapter) {
    if (lifecycleAdapter == null) {
      return this;
    }
    return new SimpleTracerBuilder(
        traceFactory, this.lifecycleAdapter.add(lifecycleAdapter), clock);
  }

  /**
   * Sets the {@link Clock} to be used by the tracer.
   *
   * @param clock the clock to use, or {@code null} to use default
   * @return a new builder instance with the given clock
   */
  public SimpleTracerBuilder withClock(@Nullable Clock clock) {
    return new SimpleTracerBuilder(traceFactory, lifecycleAdapter, clock);
  }

  /**
   * Builds a new instance of {@link SimpleTracer} using the configured options.
   *
   * @return a fully configured {@link SimpleTracer} instance
   */
  public SimpleTracer build() {
    TraceFactory traceFactory =
        this.traceFactory != null ? this.traceFactory : SimpleTraceFactory.getInstance();
    Clock clock = this.clock != null ? this.clock : Clock.systemUTC();
    return new SimpleTracer(traceFactory, lifecycleAdapter, clock);
  }
}

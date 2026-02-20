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
package io.github.malczuuu.tracekit;

import java.util.Optional;

/**
 * Main entry point for tracing operations.
 *
 * <p>This interface defines the core operations for creating, building, opening, and accessing
 * spans.
 */
public interface Tracer {

  /**
   * Creates a new root span with a new trace ID and span ID, without parent span ID.
   *
   * @return a new {@link Span} representing the root span
   */
  Span root();

  /**
   * Creates a new root span with a new trace ID and span ID, without parent span ID.
   *
   * @param name the name of the child span; must not be {@code null} or blank
   * @return a new {@link Span} representing the root span
   */
  Span root(String name);

  /**
   * Returns a builder for constructing a {@link Span}.
   *
   * <p>Use this to create spans from incoming trace information (headers, messages) or to manually
   * set trace and span IDs.
   *
   * @return a new {@link SpanBuilder}
   */
  SpanBuilder builder();

  /**
   * Returns the {@link Optional} holding currently active span, or {@link Optional#empty()} if no
   * span is active
   *
   * <p>This reflects the span most recently opened in the current thread (or execution scope in
   * reactive environments).
   *
   * @return the {@link Optional} holding current {@link Span}, or {@link Optional#empty()} if no
   *     span is active
   */
  Optional<Span> findCurrentSpan();

  /**
   * Returns the currently active span, or throws {@link NoCurrentSpanException} if no span is
   * active
   *
   * <p>This reflects the span most recently opened in the current thread (or execution scope in
   * reactive environments).
   *
   * @throws NoCurrentSpanException if no span is active
   * @return the current {@link Span}
   */
  Span getCurrentSpan() throws NoCurrentSpanException;
}

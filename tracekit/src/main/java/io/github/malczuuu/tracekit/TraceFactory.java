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

/**
 * Factory for generating trace and span identifiers.
 *
 * <p>Provides methods to create unique identifiers for distributed tracing. Typically used by
 * {@link Tracer} when creating new {@link Span} instances.
 *
 * <p>The default implementation generates 32-character hexadecimal strings for both trace IDs and
 * span IDs.
 */
public interface TraceFactory {

  /**
   * Generates a new unique trace ID.
   *
   * @return a non-null, trace ID string
   */
  String makeTraceId();

  /**
   * Generates a new unique span ID.
   *
   * @return a non-null, span ID string
   */
  String makeSpanId();
}

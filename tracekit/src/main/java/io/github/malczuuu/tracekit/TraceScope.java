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
 * Defines the propagation behavior of a {@code Span} when a {@link Traceable} method is invoked.
 *
 * <p>Analogous to transaction propagation in {@code @Transactional}:
 *
 * <ul>
 *   <li>{@link #REQUIRED} – Use the existing span if one exists, otherwise create a new root span.
 *       Nested calls create child spans.
 *   <li>{@link #REQUIRES_NEW} – Always create a new root span, independent of any current span.
 * </ul>
 *
 * @see io.github.malczuuu.tracekit.Span
 */
public enum TraceScope {

  /**
   * Use the current trace span if present; otherwise, create a new one. Nested calls create child
   * spans.
   */
  REQUIRED,

  /** Always create a new root trace span, ignoring any current one. */
  REQUIRES_NEW
}

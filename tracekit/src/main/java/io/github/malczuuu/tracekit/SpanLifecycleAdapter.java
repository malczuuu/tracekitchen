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

import org.jspecify.annotations.Nullable;

/**
 * Adapter interface for receiving lifecycle events of a {@link Span}.
 *
 * <p>Implementations can use this to perform actions when a {@link Span} is opened or closed.
 */
public interface SpanLifecycleAdapter {

  /**
   * Called after a {@link Span} has been opened.
   *
   * @param span the {@link Span} that was opened
   * @param previousSpan the {@link Span} that was suspended due to opening {@code span}
   */
  default void afterOpened(Span span, @Nullable Span previousSpan) {}

  /**
   * Called after a {@link Span} has been closed.
   *
   * @param span the {@link Span} that was closed
   * @param currentSpan the {@link Span} that was resumed after closing {@code span}
   */
  default void afterClosed(Span span, @Nullable Span currentSpan) {}
}

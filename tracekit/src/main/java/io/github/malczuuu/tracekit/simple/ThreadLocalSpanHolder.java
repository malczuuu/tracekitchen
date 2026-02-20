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
import java.util.ArrayDeque;
import java.util.Deque;
import org.jspecify.annotations.Nullable;

final class ThreadLocalSpanHolder {

  private static final ThreadLocal<Deque<Span>> STACK = ThreadLocal.withInitial(ArrayDeque::new);

  static @Nullable Span current() {
    return STACK.get().peek();
  }

  static void push(Span span) {
    STACK.get().push(span);
  }

  static @Nullable Span pop() {
    Deque<Span> deque = STACK.get();
    if (deque.isEmpty()) {
      return null;
    }

    Span span = deque.pop();

    if (deque.isEmpty()) {
      STACK.remove();
    }

    return span;
  }
}

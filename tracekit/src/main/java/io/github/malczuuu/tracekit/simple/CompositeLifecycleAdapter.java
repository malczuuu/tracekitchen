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
import io.github.malczuuu.tracekit.SpanLifecycleAdapter;
import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.Nullable;

final class CompositeLifecycleAdapter implements SpanLifecycleAdapter {

  static CompositeLifecycleAdapter empty() {
    return new CompositeLifecycleAdapter();
  }

  private final List<SpanLifecycleAdapter> delegates;

  private CompositeLifecycleAdapter() {
    this(List.of());
  }

  private CompositeLifecycleAdapter(List<SpanLifecycleAdapter> delegates) {
    this.delegates = List.copyOf(delegates);
  }

  CompositeLifecycleAdapter add(SpanLifecycleAdapter lifecycleAdapter) {
    if (delegates.contains(lifecycleAdapter)) {
      return this;
    }

    List<SpanLifecycleAdapter> copy = new ArrayList<>(delegates.size() + 1);
    copy.addAll(delegates);
    copy.add(lifecycleAdapter);

    return new CompositeLifecycleAdapter(copy);
  }

  @Override
  public void afterOpened(Span span, @Nullable Span previousSpan) {
    delegates.forEach(delegate -> delegate.afterOpened(span, previousSpan));
  }

  @Override
  public void afterClosed(Span span, @Nullable Span currentSpan) {
    delegates.forEach(delegate -> delegate.afterClosed(span, currentSpan));
  }
}

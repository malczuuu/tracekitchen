package io.github.malczuuu.tracekitchen.simple;

import io.github.malczuuu.tracekitchen.Span;
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

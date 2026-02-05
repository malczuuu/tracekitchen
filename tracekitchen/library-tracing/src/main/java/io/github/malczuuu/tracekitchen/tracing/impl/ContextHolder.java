package io.github.malczuuu.tracekitchen.tracing.impl;

import io.github.malczuuu.tracekitchen.tracing.api.Context;
import java.util.ArrayDeque;
import java.util.Deque;

final class ContextHolder {

  private static final ThreadLocal<Deque<Context>> STACK = ThreadLocal.withInitial(ArrayDeque::new);

  static Context current() {
    return STACK.get().peek();
  }

  static void push(Context context) {
    STACK.get().push(context);
  }

  static void pop() {
    Deque<Context> deque = STACK.get();
    if (deque == null || deque.isEmpty()) {
      return;
    }

    deque.pop();

    if (deque.isEmpty()) {
      STACK.remove();
    }
  }
}

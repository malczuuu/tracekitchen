package io.github.malczuuu.tracekitchen.tracing.core;

import java.util.ArrayDeque;
import java.util.Deque;
import org.jspecify.annotations.Nullable;

final class ContextThreadLocalHolder {

  private static final ThreadLocal<Deque<TraceContext>> STACK =
      ThreadLocal.withInitial(ArrayDeque::new);

  static @Nullable TraceContext current() {
    return STACK.get().peek();
  }

  static void push(TraceContext context) {
    STACK.get().push(context);
  }

  static @Nullable TraceContext pop() {
    Deque<TraceContext> deque = STACK.get();
    if (deque.isEmpty()) {
      return null;
    }

    TraceContext context = deque.pop();

    if (deque.isEmpty()) {
      STACK.remove();
    }

    return context;
  }
}

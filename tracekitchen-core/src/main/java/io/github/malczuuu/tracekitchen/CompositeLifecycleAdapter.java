package io.github.malczuuu.tracekitchen;

import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.Nullable;

final class CompositeLifecycleAdapter implements TraceContextLifecycleAdapter {

  static CompositeLifecycleAdapter empty() {
    return new CompositeLifecycleAdapter();
  }

  private final List<TraceContextLifecycleAdapter> delegates;

  private CompositeLifecycleAdapter() {
    this(List.of());
  }

  private CompositeLifecycleAdapter(List<TraceContextLifecycleAdapter> delegates) {
    this.delegates = List.copyOf(delegates);
  }

  CompositeLifecycleAdapter add(TraceContextLifecycleAdapter lifecycleAdapter) {
    if (delegates.contains(lifecycleAdapter)) {
      return this;
    }

    List<TraceContextLifecycleAdapter> copy = new ArrayList<>(delegates.size() + 1);
    copy.addAll(delegates);
    copy.add(lifecycleAdapter);

    return new CompositeLifecycleAdapter(copy);
  }

  @Override
  public void afterOpened(TraceContext context, @Nullable TraceContext previousContext) {
    delegates.forEach(delegate -> delegate.afterOpened(context, previousContext));
  }

  @Override
  public void afterClosed(TraceContext context, @Nullable TraceContext currentContext) {
    delegates.forEach(delegate -> delegate.afterClosed(context, currentContext));
  }
}

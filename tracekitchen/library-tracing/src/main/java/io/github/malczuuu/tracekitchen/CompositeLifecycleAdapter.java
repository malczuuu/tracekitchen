package io.github.malczuuu.tracekitchen;

import java.util.ArrayList;
import java.util.List;

class CompositeLifecycleAdapter implements TraceContextLifecycleAdapter {

  private final List<TraceContextLifecycleAdapter> delegates;

  CompositeLifecycleAdapter() {
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
  public void afterOpened(TraceContext context) {
    delegates.forEach(delegate -> delegate.afterOpened(context));
  }

  @Override
  public void afterClosed(TraceContext context) {
    delegates.forEach(delegate -> delegate.afterClosed(context));
  }
}

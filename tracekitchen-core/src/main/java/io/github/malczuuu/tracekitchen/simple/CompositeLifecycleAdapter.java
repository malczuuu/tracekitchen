package io.github.malczuuu.tracekitchen.simple;

import io.github.malczuuu.tracekitchen.Span;
import io.github.malczuuu.tracekitchen.SpanLifecycleAdapter;
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

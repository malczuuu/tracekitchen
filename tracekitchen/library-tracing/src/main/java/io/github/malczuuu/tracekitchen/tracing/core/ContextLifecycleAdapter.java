package io.github.malczuuu.tracekitchen.tracing.core;

public interface ContextLifecycleAdapter {

  void onContextOpened(TraceContext context);

  void onContextClosed(TraceContext context);
}

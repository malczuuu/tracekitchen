package io.github.malczuuu.tracekitchen.tracing.core;

class NoOpLifecycleAdapter implements ContextLifecycleAdapter {

  static ContextLifecycleAdapter getInstance() {
    return Holder.INSTANCE;
  }

  private NoOpLifecycleAdapter() {}

  @Override
  public void onContextOpened(TraceContext context) {}

  @Override
  public void onContextClosed(TraceContext context) {}

  private static final class Holder {
    private static final NoOpLifecycleAdapter INSTANCE = new NoOpLifecycleAdapter();
  }
}

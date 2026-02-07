package io.github.malczuuu.tracekitchen;

class NoOpLoggingAdapter implements LoggingContextAdapter {

  static LoggingContextAdapter getInstance() {
    return Holder.INSTANCE;
  }

  private NoOpLoggingAdapter() {}

  @Override
  public void push(TraceContext context) {}

  @Override
  public void drop() {}

  private static final class Holder {
    private static final NoOpLoggingAdapter INSTANCE = new NoOpLoggingAdapter();
  }
}

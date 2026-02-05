package io.github.malczuuu.tracekitchen.tracing.api;

public interface OpenContext extends AutoCloseable {

  Context getContext();

  @Override
  void close();
}

package io.github.malczuuu.tracekitchen.tracing.impl;

import io.github.malczuuu.tracekitchen.tracing.api.Context;
import io.github.malczuuu.tracekitchen.tracing.api.OpenContext;

final class OpenContextWrapper implements OpenContext {

  private final Context context;
  private final Runnable close;

  OpenContextWrapper(Context context, Runnable close) {
    this.context = context;
    this.close = close;
  }

  @Override
  public Context getContext() {
    return context;
  }

  @Override
  public void close() {
    close.run();
  }
}

package io.github.malczuuu.tracekitchen.tracing.api;

public interface Tracer {

  Context createContext();

  OpenContext openContext(Context context);

  Context getCurrentContext();
}

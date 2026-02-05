package io.github.malczuuu.tracekitchen.tracing.api;

public interface Context {

  Context makeChild();

  String getTraceId();

  String getSpanId();

  String getParentSpanId();
}

package io.github.malczuuu.tracekit.boot4.webmvc;

import io.github.malczuuu.tracekit.boot4.TraceHeaderSettings;
import java.util.List;

final class DummyHeaderSettings implements TraceHeaderSettings {

  private final List<String> traceIdHeaderNames;
  private final List<String> spanIdHeaderNames;
  private final List<String> parentIdHeaderNames;

  DummyHeaderSettings(
      List<String> traceIdHeaderNames,
      List<String> spanIdHeaderNames,
      List<String> parentIdHeaderNames) {
    this.traceIdHeaderNames = traceIdHeaderNames;
    this.spanIdHeaderNames = spanIdHeaderNames;
    this.parentIdHeaderNames = parentIdHeaderNames;
  }

  @Override
  public List<String> getTraceIdHeaderNames() {
    return traceIdHeaderNames;
  }

  @Override
  public List<String> getSpanIdHeaderNames() {
    return spanIdHeaderNames;
  }

  @Override
  public List<String> getParentIdHeaderNames() {
    return parentIdHeaderNames;
  }
}

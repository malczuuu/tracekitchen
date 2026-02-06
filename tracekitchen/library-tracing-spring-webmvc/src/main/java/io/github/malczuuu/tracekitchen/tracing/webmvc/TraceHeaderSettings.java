package io.github.malczuuu.tracekitchen.tracing.webmvc;

import java.util.List;

public interface TraceHeaderSettings {

  List<String> getTraceIdHeaderNames();

  List<String> getSpanIdHeaderNames();

  List<String> getParentIdHeaderNames();
}

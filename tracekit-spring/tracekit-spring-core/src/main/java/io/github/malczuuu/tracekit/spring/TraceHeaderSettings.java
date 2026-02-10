package io.github.malczuuu.tracekit.spring;

import java.util.List;

public interface TraceHeaderSettings {

  List<String> getTraceIdHeaderNames();

  List<String> getSpanIdHeaderNames();

  List<String> getParentIdHeaderNames();
}

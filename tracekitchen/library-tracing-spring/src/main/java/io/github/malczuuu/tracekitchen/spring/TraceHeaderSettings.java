package io.github.malczuuu.tracekitchen.spring;

import java.util.List;

public interface TraceHeaderSettings {

  List<String> getTraceIdHeaderNames();

  List<String> getSpanIdHeaderNames();

  List<String> getParentIdHeaderNames();
}

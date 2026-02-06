package io.github.malczuuu.tracekitchen.tracing.webmvc.autoconfigure;

import io.github.malczuuu.tracekitchen.tracing.webmvc.TraceHeaderSettings;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "tracekitchen.webmvc")
public class TracingWebMvcProperties implements TraceHeaderSettings {

  private final String traceIdHeaderNames;
  private final String spanIdHeaderNames;
  private final String parentSpanIdHeaderNames;

  public TracingWebMvcProperties(
      @DefaultValue("") String traceIdHeaderNames,
      @DefaultValue("") String spanIdHeaderNames,
      @DefaultValue("") String parentSpanIdHeaderNames) {
    this.traceIdHeaderNames = traceIdHeaderNames;
    this.spanIdHeaderNames = spanIdHeaderNames;
    this.parentSpanIdHeaderNames = parentSpanIdHeaderNames;
  }

  @Override
  public List<String> getTraceIdHeaderNames() {
    return Stream.of(traceIdHeaderNames.split(","))
        .map(String::strip)
        .filter(s -> !s.isBlank())
        .toList();
  }

  @Override
  public List<String> getSpanIdHeaderNames() {
    return Stream.of(spanIdHeaderNames.split(","))
        .map(String::strip)
        .filter(s -> !s.isBlank())
        .toList();
  }

  @Override
  public List<String> getParentIdHeaderNames() {
    return Stream.of(parentSpanIdHeaderNames.split(","))
        .map(String::strip)
        .filter(s -> !s.isBlank())
        .toList();
  }
}

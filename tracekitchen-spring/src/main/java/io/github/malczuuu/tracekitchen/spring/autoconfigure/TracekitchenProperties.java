package io.github.malczuuu.tracekitchen.spring.autoconfigure;

import io.github.malczuuu.tracekitchen.spring.TraceHeaderSettings;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "tracekitchen")
public class TracekitchenProperties implements TraceHeaderSettings {

  private final String traceIdHeaderNames;
  private final String spanIdHeaderNames;
  private final String parentSpanIdHeaderNames;

  public TracekitchenProperties(
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

package io.github.malczuuu.tracekit.boot4.autoconfigure;

import io.github.malczuuu.tracekit.boot4.TraceHeaderSettings;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/** Configuration properties for TraceKit Core module. */
@ConfigurationProperties(prefix = "tracekit")
public class TracekitProperties implements TraceHeaderSettings {

  private final boolean enabled;
  private final String traceIdHeaderNames;
  private final String spanIdHeaderNames;
  private final String parentSpanIdHeaderNames;

  /**
   * Creates a new {@link TracekitProperties} instance with the given header names.
   *
   * @param enabled whether TraceKit module should be enabled; if {@code null}, defaults to {@code
   *     true}
   * @param traceIdHeaderNames comma-separated list of trace ID header names
   * @param spanIdHeaderNames comma-separated list of span ID header names
   * @param parentSpanIdHeaderNames comma-separated list of parent span ID header names
   */
  public TracekitProperties(
      @DefaultValue("true") boolean enabled,
      @DefaultValue("") String traceIdHeaderNames,
      @DefaultValue("") String spanIdHeaderNames,
      @DefaultValue("") String parentSpanIdHeaderNames) {
    this.enabled = enabled;
    this.traceIdHeaderNames = traceIdHeaderNames;
    this.spanIdHeaderNames = spanIdHeaderNames;
    this.parentSpanIdHeaderNames = parentSpanIdHeaderNames;
  }

  /**
   * Returns whether TraceKit module is enabled.
   *
   * @return {@code true} if module enabled, {@code false} otherwise
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Returns the list of configured HTTP header names used to propagate the trace ID.
   *
   * @return a list of trace ID header names, empty if none are configured
   */
  @Override
  public List<String> getTraceIdHeaderNames() {
    return Stream.of(traceIdHeaderNames.split(","))
        .map(String::strip)
        .filter(s -> !s.isBlank())
        .toList();
  }

  /**
   * Returns the list of configured HTTP header names used to propagate the current span ID.
   *
   * @return a list of span ID header names, empty if none are configured
   */
  @Override
  public List<String> getSpanIdHeaderNames() {
    return Stream.of(spanIdHeaderNames.split(","))
        .map(String::strip)
        .filter(s -> !s.isBlank())
        .toList();
  }

  /**
   * Returns the list of configured HTTP header names used to propagate the parent span ID.
   *
   * @return a list of parent span ID header names, empty if none are configured
   */
  @Override
  public List<String> getParentIdHeaderNames() {
    return Stream.of(parentSpanIdHeaderNames.split(","))
        .map(String::strip)
        .filter(s -> !s.isBlank())
        .toList();
  }
}

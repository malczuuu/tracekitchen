package io.github.malczuuu.tracekit.spring.autoconfigure;

import io.github.malczuuu.tracekit.spring.TraceHeaderSettings;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * {@code TracekitProperties} is a Spring {@link ConfigurationProperties} that provides
 * configuration for the HTTP headers used for propagating tracing information.
 *
 * <p>The class reads comma-separated header names from configuration properties prefixed with
 * {@code tracekit} and exposes them as lists for use by tracing libraries or HTTP clients.
 */
@ConfigurationProperties(prefix = "tracekit")
public class TracekitProperties implements TraceHeaderSettings {

  private final String traceIdHeaderNames;
  private final String spanIdHeaderNames;
  private final String parentSpanIdHeaderNames;

  /**
   * Creates a new {@link TracekitProperties} instance with the given header names.
   *
   * @param traceIdHeaderNames comma-separated list of trace ID header names
   * @param spanIdHeaderNames comma-separated list of span ID header names
   * @param parentSpanIdHeaderNames comma-separated list of parent span ID header names
   */
  public TracekitProperties(
      @DefaultValue("") String traceIdHeaderNames,
      @DefaultValue("") String spanIdHeaderNames,
      @DefaultValue("") String parentSpanIdHeaderNames) {
    this.traceIdHeaderNames = traceIdHeaderNames;
    this.spanIdHeaderNames = spanIdHeaderNames;
    this.parentSpanIdHeaderNames = parentSpanIdHeaderNames;
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

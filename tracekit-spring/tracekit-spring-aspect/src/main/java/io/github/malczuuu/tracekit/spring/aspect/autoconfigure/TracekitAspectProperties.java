package io.github.malczuuu.tracekit.spring.aspect.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/** Configuration properties for TraceKit Aspect module. */
@ConfigurationProperties(prefix = "tracekit.aspect")
public class TracekitAspectProperties {

  private final boolean enabled;
  private final boolean scheduledEnabled;
  private final boolean traceableEnabled;

  /**
   * Creates a new {@link TracekitAspectProperties}.
   *
   * @param enabled whether TraceKit Aspect module should be enabled; if {@code null}, defaults to
   *     {@code true}
   * @param scheduledEnabled whether {@code ScheduledTracingAspect} is enabled
   * @param traceableEnabled whether {@code TraceableTracingAspect} is enabled
   */
  public TracekitAspectProperties(
      @DefaultValue("true") boolean enabled,
      @DefaultValue("true") boolean scheduledEnabled,
      @DefaultValue("true") boolean traceableEnabled) {
    this.enabled = enabled;
    this.scheduledEnabled = scheduledEnabled;
    this.traceableEnabled = traceableEnabled;
  }

  /**
   * Returns whether TraceKit Aspect module is enabled.
   *
   * @return {@code true} if module enabled, {@code false} otherwise
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Returns whether {@code ScheduledTracingAspect} should be created.
   *
   * @return {@code true} if {@code ScheduledTracingAspect} should be created, {@code false}
   *     otherwise
   */
  public boolean isScheduledEnabled() {
    return scheduledEnabled;
  }

  /**
   * Returns whether {@code TraceableTracingAspect} should be created.
   *
   * @return {@code true} if {@code TraceableTracingAspect} should be created, {@code false}
   *     otherwise
   */
  public boolean isTraceableEnabled() {
    return traceableEnabled;
  }
}

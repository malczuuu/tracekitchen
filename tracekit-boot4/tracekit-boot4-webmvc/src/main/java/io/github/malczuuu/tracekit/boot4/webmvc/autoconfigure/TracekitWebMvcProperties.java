package io.github.malczuuu.tracekit.boot4.webmvc.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/** Configuration properties for the Tracekit WebMVC module. */
@ConfigurationProperties(prefix = "tracekit.webmvc")
public class TracekitWebMvcProperties {

  private final boolean enabled;

  /**
   * Creates a new {@link TracekitWebMvcProperties} instance with the given header names.
   *
   * @param enabled whether TraceKit WebMVC module should be enabled; if {@code null}, defaults to
   *     {@code true}
   */
  public TracekitWebMvcProperties(@DefaultValue("true") boolean enabled) {
    this.enabled = enabled;
  }

  /**
   * Returns whether TraceKit WebMVC module is enabled.
   *
   * @return {@code true} if module enabled, {@code false} otherwise
   */
  public boolean isEnabled() {
    return enabled;
  }
}

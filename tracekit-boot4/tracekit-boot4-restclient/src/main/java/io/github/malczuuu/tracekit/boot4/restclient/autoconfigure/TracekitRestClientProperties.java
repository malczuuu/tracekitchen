package io.github.malczuuu.tracekit.boot4.restclient.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/** Configuration properties for the Tracekit RestClient module. */
@ConfigurationProperties(prefix = "tracekit.restclient")
public class TracekitRestClientProperties {

  private final boolean enabled;

  /**
   * Creates a new {@link TracekitRestClientProperties} instance with the given header names.
   *
   * @param enabled whether Tracekit RestClient module should be enabled; if {@code null}, defaults
   *     to {@code true}
   */
  public TracekitRestClientProperties(@DefaultValue("true") boolean enabled) {
    this.enabled = enabled;
  }

  /**
   * Returns whether Tracekit RestClient module is enabled.
   *
   * @return {@code true} if module enabled, {@code false} otherwise
   */
  public boolean isEnabled() {
    return enabled;
  }
}

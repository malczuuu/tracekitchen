package io.github.malczuuu.tracekitchen.spring.aspect.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/** Configuration properties for TraceKitchen Aspect module. */
@ConfigurationProperties(prefix = "tracekitchen.aspect")
public class TracekitchenAspectProperties {

  private final boolean enabled;

  /**
   * Creates a new {@link TracekitchenAspectProperties}.
   *
   * @param enabled whether TraceKitchen Aspect module should be enabled; if {@code null}, defaults
   *     to {@code true}
   */
  public TracekitchenAspectProperties(@DefaultValue("true") boolean enabled) {
    this.enabled = enabled;
  }

  /**
   * Returns whether TraceKitchen Aspect module is enabled.
   *
   * @return {@code true} if module enabled, {@code false} otherwise
   */
  public boolean isEnabled() {
    return enabled;
  }
}

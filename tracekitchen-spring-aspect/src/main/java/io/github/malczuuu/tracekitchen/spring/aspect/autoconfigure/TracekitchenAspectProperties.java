package io.github.malczuuu.tracekitchen.spring.aspect.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "tracekitchen.aspect")
public class TracekitchenAspectProperties {

  private final boolean enabled;

  public TracekitchenAspectProperties(@DefaultValue("true") boolean enabled) {
    this.enabled = enabled;
  }

  public boolean isEnabled() {
    return enabled;
  }
}

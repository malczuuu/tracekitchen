package io.github.malczuuu.tracekit.spring.webmvc.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "tracekitchen.webmvc")
public class TracekitchenWebMvcProperties {

  private final boolean enabled;

  public TracekitchenWebMvcProperties(@DefaultValue("true") boolean enabled) {
    this.enabled = enabled;
  }

  public boolean isEnabled() {
    return enabled;
  }
}

package io.github.malczuuu.tracekit.spring.webmvc.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "tracekit.webmvc")
public class TracekitWebMvcProperties {

  private final boolean enabled;

  public TracekitWebMvcProperties(@DefaultValue("true") boolean enabled) {
    this.enabled = enabled;
  }

  public boolean isEnabled() {
    return enabled;
  }
}

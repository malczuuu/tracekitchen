package io.github.malczuuu.tracekit.spring.restclient.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "tracekitchen.restclient")
public class TracekitchenRestClientProperties {

  private final boolean enabled;

  public TracekitchenRestClientProperties(@DefaultValue("true") boolean enabled) {
    this.enabled = enabled;
  }

  public boolean isEnabled() {
    return enabled;
  }
}

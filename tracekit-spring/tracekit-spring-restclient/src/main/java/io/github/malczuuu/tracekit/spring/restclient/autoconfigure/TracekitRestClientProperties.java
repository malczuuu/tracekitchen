package io.github.malczuuu.tracekit.spring.restclient.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "tracekit.restclient")
public class TracekitRestClientProperties {

  private final boolean enabled;

  public TracekitRestClientProperties(@DefaultValue("true") boolean enabled) {
    this.enabled = enabled;
  }

  public boolean isEnabled() {
    return enabled;
  }
}

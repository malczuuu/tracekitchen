package io.github.malczuuu.tracekit.common.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "tracekit.common")
public class CommonProperties {

  private final String openapiTitle;

  public CommonProperties(@DefaultValue("") String openapiTitle) {
    this.openapiTitle = openapiTitle;
  }

  public String getOpenapiTitle() {
    return openapiTitle;
  }
}

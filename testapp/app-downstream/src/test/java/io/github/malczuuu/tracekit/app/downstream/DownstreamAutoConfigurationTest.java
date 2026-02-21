package io.github.malczuuu.tracekit.app.downstream;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.malczuuu.tracekit.boot4.aspect.autoconfigure.TracekitAspectAutoConfiguration;
import io.github.malczuuu.tracekit.boot4.autoconfigure.TracekitAutoConfiguration;
import io.github.malczuuu.tracekit.boot4.restclient.autoconfigure.TracekitRestClientAutoConfiguration;
import io.github.malczuuu.tracekit.boot4.webmvc.autoconfigure.TracekitWebMvcAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = {DownstreamApplication.class})
@ActiveProfiles(profiles = {"test"})
public class DownstreamAutoConfigurationTest {

  @Autowired(required = false)
  private TracekitAutoConfiguration tracekitAutoConfiguration;

  @Autowired(required = false)
  private TracekitAspectAutoConfiguration tracekitAspectAutoConfiguration;

  @Autowired(required = false)
  private TracekitRestClientAutoConfiguration tracekitRestClientAutoConfiguration;

  @Autowired(required = false)
  private TracekitWebMvcAutoConfiguration tracekitWebMvcAutoConfiguration;

  @Test
  void shouldLoadTracekitAutoConfiguration() {
    assertThat(tracekitAutoConfiguration).isNotNull();
    assertThat(tracekitAspectAutoConfiguration).isNotNull();
    assertThat(tracekitRestClientAutoConfiguration).isNotNull();
    assertThat(tracekitWebMvcAutoConfiguration).isNotNull();
  }
}

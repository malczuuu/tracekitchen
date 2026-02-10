package io.github.malczuuu.tracekit.spring.restclient.autoconfigure;

import io.github.malczuuu.tracekit.spring.autoconfigure.TracekitAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
    classes = {TracekitAutoConfiguration.class, TracekitRestClientAutoConfiguration.class})
@ActiveProfiles(profiles = {"test"})
class TracekitRestClientAutoConfigurationTest {

  @Test
  void contextLoads() {}
}

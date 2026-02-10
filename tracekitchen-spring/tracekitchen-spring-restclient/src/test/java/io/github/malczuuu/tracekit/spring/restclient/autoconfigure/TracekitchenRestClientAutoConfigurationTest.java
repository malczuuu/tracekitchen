package io.github.malczuuu.tracekit.spring.restclient.autoconfigure;

import io.github.malczuuu.tracekit.spring.autoconfigure.TracekitchenAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
    classes = {TracekitchenAutoConfiguration.class, TracekitchenRestClientAutoConfiguration.class})
@ActiveProfiles(profiles = {"test"})
class TracekitchenRestClientAutoConfigurationTest {

  @Test
  void contextLoads() {}
}

package io.github.malczuuu.tracekitchen.tracing.webmvc.autoconfigure;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = {TracingWebMvcAutoConfiguration.class})
@ActiveProfiles(profiles = {"test"})
class TracingWebMvcAutoConfigurationTest {

  @Test
  void contextLoads() {}
}

package io.github.malczuuu.tracekit.spring.webmvc.autoconfigure;

import io.github.malczuuu.tracekit.spring.autoconfigure.TracekitAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = {TracekitAutoConfiguration.class, TracekitWebMvcAutoConfiguration.class})
@ActiveProfiles(profiles = {"test"})
class TracekitWebMvcAutoConfigurationTest {

  @Test
  void contextLoads() {}
}

package io.github.malczuuu.tracekit.spring.webmvc.autoconfigure;

import io.github.malczuuu.tracekit.spring.autoconfigure.TracekitchenAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
    classes = {TracekitchenAutoConfiguration.class, TracekitchenWebMvcAutoConfiguration.class})
@ActiveProfiles(profiles = {"test"})
class TracekitchenWebMvcAutoConfigurationTest {

  @Test
  void contextLoads() {}
}

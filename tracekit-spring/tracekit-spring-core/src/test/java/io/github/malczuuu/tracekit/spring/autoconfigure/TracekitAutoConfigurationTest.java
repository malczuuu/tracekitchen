package io.github.malczuuu.tracekit.spring.autoconfigure;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = {TracekitAutoConfiguration.class})
@ActiveProfiles(profiles = {"test"})
class TracekitAutoConfigurationTest {

  @Test
  void contextLoads() {}
}

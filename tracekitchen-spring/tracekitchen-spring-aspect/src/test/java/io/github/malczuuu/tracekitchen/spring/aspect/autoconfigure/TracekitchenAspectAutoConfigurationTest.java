package io.github.malczuuu.tracekitchen.spring.aspect.autoconfigure;

import io.github.malczuuu.tracekitchen.spring.autoconfigure.TracekitchenAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
    classes = {TracekitchenAutoConfiguration.class, TracekitchenAspectAutoConfiguration.class})
@ActiveProfiles(profiles = {"test"})
class TracekitchenAspectAutoConfigurationTest {

  @Test
  void contextLoads() {}
}

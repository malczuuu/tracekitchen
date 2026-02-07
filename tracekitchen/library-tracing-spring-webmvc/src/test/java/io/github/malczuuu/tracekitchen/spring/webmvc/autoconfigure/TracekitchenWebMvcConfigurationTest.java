package io.github.malczuuu.tracekitchen.spring.webmvc.autoconfigure;

import io.github.malczuuu.tracekitchen.spring.autoconfigure.TracekitchenAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
    classes = {TracekitchenAutoConfiguration.class, TracekitchenWebMvcConfiguration.class})
@ActiveProfiles(profiles = {"test"})
class TracekitchenWebMvcConfigurationTest {

  @Test
  void contextLoads() {}
}

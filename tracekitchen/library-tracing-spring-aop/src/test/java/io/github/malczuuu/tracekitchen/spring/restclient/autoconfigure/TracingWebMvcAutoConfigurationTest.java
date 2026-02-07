package io.github.malczuuu.tracekitchen.spring.restclient.autoconfigure;

import io.github.malczuuu.tracekitchen.spring.aop.autoconfigure.TracekitchenAspectAutoConfiguration;
import io.github.malczuuu.tracekitchen.spring.autoconfigure.TracekitchenAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
    classes = {TracekitchenAutoConfiguration.class, TracekitchenAspectAutoConfiguration.class})
@ActiveProfiles(profiles = {"test"})
class TracingWebMvcAutoConfigurationTest {

  @Test
  void contextLoads() {}
}

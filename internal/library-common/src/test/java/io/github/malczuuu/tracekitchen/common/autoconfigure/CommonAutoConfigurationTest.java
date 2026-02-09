package io.github.malczuuu.tracekitchen.common.autoconfigure;

import io.github.malczuuu.tracekitchen.spring.autoconfigure.TracekitchenAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.restclient.autoconfigure.RestClientAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
    classes = {
      CommonAutoConfiguration.class,
      RestClientAutoConfiguration.class,
      TracekitchenAutoConfiguration.class
    })
@ActiveProfiles(profiles = {"test"})
class CommonAutoConfigurationTest {

  @Test
  void contextLoads() {}
}

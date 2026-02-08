package io.github.malczuuu.tracekitchen.app.downstream;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = {DownstreamApplication.class})
@ActiveProfiles(profiles = {"test"})
class DownstreamApplicationTest {

  @Test
  void contextLoads() {}
}

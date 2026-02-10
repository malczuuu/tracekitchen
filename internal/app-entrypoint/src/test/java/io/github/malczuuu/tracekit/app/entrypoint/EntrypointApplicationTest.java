package io.github.malczuuu.tracekit.app.entrypoint;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = {EntrypointApplication.class})
@ActiveProfiles(profiles = {"test"})
class EntrypointApplicationTest {

  @Test
  void contextLoads() {}
}

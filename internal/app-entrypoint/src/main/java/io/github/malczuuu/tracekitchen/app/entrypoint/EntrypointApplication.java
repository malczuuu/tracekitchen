package io.github.malczuuu.tracekitchen.app.entrypoint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAspectJAutoProxy
@EnableAsync
@EnableScheduling
@SpringBootApplication
public class EntrypointApplication {

  public static void main(String[] args) {
    SpringApplication.run(EntrypointApplication.class, args);
  }
}

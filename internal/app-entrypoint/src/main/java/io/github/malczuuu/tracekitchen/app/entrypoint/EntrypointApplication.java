package io.github.malczuuu.tracekitchen.app.entrypoint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class EntrypointApplication {

  public static void main(String[] args) {
    SpringApplication.run(EntrypointApplication.class, args);
  }
}

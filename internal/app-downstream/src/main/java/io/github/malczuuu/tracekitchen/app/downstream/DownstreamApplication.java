package io.github.malczuuu.tracekitchen.app.downstream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class DownstreamApplication {

  public static void main(String[] args) {
    SpringApplication.run(DownstreamApplication.class, args);
  }
}

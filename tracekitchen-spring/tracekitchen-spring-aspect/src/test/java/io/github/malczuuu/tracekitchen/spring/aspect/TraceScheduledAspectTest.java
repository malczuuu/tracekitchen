package io.github.malczuuu.tracekitchen.spring.aspect;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.malczuuu.tracekitchen.OpenSpan;
import io.github.malczuuu.tracekitchen.Span;
import io.github.malczuuu.tracekitchen.Tracer;
import io.github.malczuuu.tracekitchen.spring.aspect.autoconfigure.TracekitchenAspectAutoConfiguration;
import io.github.malczuuu.tracekitchen.spring.autoconfigure.TracekitchenAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(profiles = {"test"})
@EnableAspectJAutoProxy
@SpringBootTest(
    classes = {
      TracekitchenAutoConfiguration.class,
      TracekitchenAspectAutoConfiguration.class,
      TraceScheduledAspectTest.DummyScheduledService.class
    })
class TraceScheduledAspectTest {

  @Autowired private DummyScheduledService service;
  @Autowired private Tracer tracer;

  @Test
  void givenNoContext_whenCallingScheduledMethod_shouldSpawnNewRootContext() {
    assertThat(tracer.getCurrentSpan()).isNull();

    Span result = service.scheduledWithoutContext();

    assertThat(result).isNotNull();
    assertThat(result.getName()).isEqualTo("DummyScheduledService.scheduledWithoutContext");
    assertThat(result.getTrace().getTraceId()).isNotNull();
    assertThat(result.getTrace().getSpanId()).isNotNull();
    assertThat(result.getTrace().getParentSpanId()).isNull();

    assertThat(tracer.getCurrentSpan()).isNull();
  }

  @Test
  void givenOpenContext_whenCallingScheduledMethod_shouldReuseExistingContext() {
    Span parent = tracer.root("parent");
    try (OpenSpan ignored = parent.open()) {

      Span result = service.scheduledWithParentContext();

      assertThat(result).isNotNull();
      assertThat(result.getName()).isEqualTo("DummyScheduledService.scheduledWithParentContext");
      assertThat(result.getTrace().getTraceId()).isNotNull();
      assertThat(result.getTrace().getTraceId()).isEqualTo(parent.getTrace().getTraceId());
      assertThat(result.getTrace().getSpanId()).isNotNull();
      assertThat(result.getTrace().getSpanId()).isNotEqualTo(parent.getTrace().getSpanId());
      assertThat(result.getTrace().getParentSpanId()).isEqualTo(parent.getTrace().getSpanId());

      assertThat(tracer.getCurrentSpan()).isEqualTo(parent);
    }
  }

  @Service
  public static class DummyScheduledService {

    private final Tracer tracer;

    public DummyScheduledService(Tracer tracer) {
      this.tracer = tracer;
    }

    @Scheduled(fixedDelay = 1000)
    public Span scheduledWithoutContext() {
      return tracer.getCurrentSpan();
    }

    @Scheduled(fixedDelay = 1000)
    public Span scheduledWithParentContext() {
      return tracer.getCurrentSpan();
    }
  }
}

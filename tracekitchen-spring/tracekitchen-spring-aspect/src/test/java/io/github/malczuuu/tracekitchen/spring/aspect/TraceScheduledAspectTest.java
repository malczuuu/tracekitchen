package io.github.malczuuu.tracekitchen.spring.aspect;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.malczuuu.tracekitchen.OpenContext;
import io.github.malczuuu.tracekitchen.TraceContext;
import io.github.malczuuu.tracekitchen.TraceContextSnapshot;
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
    assertThat(tracer.getCurrentContext()).isNull();

    TraceContextSnapshot result = service.scheduledWithoutContext();

    assertThat(result).isNotNull();
    assertThat(result.getName()).isEqualTo("DummyScheduledService.scheduledWithoutContext");
    assertThat(result.getTraceId()).isNotNull();
    assertThat(result.getSpanId()).isNotNull();
    assertThat(result.getParentSpanId()).isNull();

    assertThat(tracer.getCurrentContext()).isNull();
  }

  @Test
  void givenOpenContext_whenCallingScheduledMethod_shouldReuseExistingContext() {
    TraceContext parent = tracer.newRootContext("parent");
    try (OpenContext ignored = parent.open()) {

      TraceContextSnapshot result = service.scheduledWithParentContext();

      assertThat(result).isNotNull();
      assertThat(result.getName()).isEqualTo("DummyScheduledService.scheduledWithParentContext");
      assertThat(result.getTraceId()).isNotNull();
      assertThat(result.getTraceId()).isEqualTo(parent.getTraceId());
      assertThat(result.getSpanId()).isNotNull();
      assertThat(result.getSpanId()).isNotEqualTo(parent.getSpanId());
      assertThat(result.getParentSpanId()).isEqualTo(parent.getSpanId());

      assertThat(tracer.getCurrentContext()).isEqualTo(parent);
    }
  }

  @Service
  public static class DummyScheduledService {

    private final Tracer tracer;

    public DummyScheduledService(Tracer tracer) {
      this.tracer = tracer;
    }

    @Scheduled(fixedDelay = 1000)
    public TraceContextSnapshot scheduledWithoutContext() {
      return tracer.getCurrentContext();
    }

    @Scheduled(fixedDelay = 1000)
    public TraceContextSnapshot scheduledWithParentContext() {
      return tracer.getCurrentContext();
    }
  }
}

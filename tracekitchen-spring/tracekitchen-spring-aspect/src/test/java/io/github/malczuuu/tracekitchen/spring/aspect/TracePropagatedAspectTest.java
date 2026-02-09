package io.github.malczuuu.tracekitchen.spring.aspect;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.malczuuu.tracekitchen.OpenContext;
import io.github.malczuuu.tracekitchen.TraceContext;
import io.github.malczuuu.tracekitchen.Tracer;
import io.github.malczuuu.tracekitchen.annotation.TraceScope;
import io.github.malczuuu.tracekitchen.annotation.Traceable;
import io.github.malczuuu.tracekitchen.spring.aspect.autoconfigure.TracekitchenAspectAutoConfiguration;
import io.github.malczuuu.tracekitchen.spring.autoconfigure.TracekitchenAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(profiles = {"test"})
@EnableAspectJAutoProxy
@SpringBootTest(
    classes = {
      TracekitchenAutoConfiguration.class,
      TracekitchenAspectAutoConfiguration.class,
      TracePropagatedAspectTest.DummyService.class
    })
class TracePropagatedAspectTest {

  @Autowired private DummyService dummyService;
  @Autowired private Tracer tracer;

  @Test
  void givenNoContext_whenCallingTraceableMethodWithoutName_shouldSpawnNewContext() {
    assertThat(tracer.getCurrentContext()).isNull();

    TraceContext result = dummyService.traceableDefaultWithoutName();

    assertThat(result).isNotNull();
    assertThat(result.getName()).isEqualTo("DummyService.traceableDefaultWithoutName");
    assertThat(result.getTraceId()).isNotNull();
    assertThat(result.getSpanId()).isNotNull();
    assertThat(result.getParentSpanId()).isNull();

    assertThat(tracer.getCurrentContext()).isNull();
  }

  @Test
  void givenNoContext_whenCallingTraceableMethodWithName_shouldSpawnNewContext() {
    assertThat(tracer.getCurrentContext()).isNull();

    TraceContext result = dummyService.traceableDefaultWithName();

    assertThat(result).isNotNull();
    assertThat(result.getName()).isEqualTo("nameOfTraceableDefault");
    assertThat(result.getTraceId()).isNotNull();
    assertThat(result.getSpanId()).isNotNull();
    assertThat(result.getParentSpanId()).isNull();

    assertThat(tracer.getCurrentContext()).isNull();
  }

  @Test
  void givenOpenContext_whenCallingTraceableMethodWithoutName_shouldSpawnChild() {
    TraceContext parent = tracer.newRootContext("parent");
    try (OpenContext ignored = tracer.open(parent)) {
      TraceContext result = dummyService.traceableDefaultWithoutName();

      assertThat(result).isNotNull();
      assertThat(result.getName()).isEqualTo("DummyService.traceableDefaultWithoutName");
      assertThat(result.getTraceId()).isNotNull();
      assertThat(result.getTraceId()).isEqualTo(parent.getTraceId());
      assertThat(result.getSpanId()).isNotNull();
      assertThat(result.getSpanId()).isNotEqualTo(parent.getSpanId());
      assertThat(result.getParentSpanId()).isEqualTo(parent.getSpanId());

      assertThat(tracer.getCurrentContext()).isEqualTo(parent);
    }
  }

  @Test
  void givenOpenContext_whenCallingTraceableMethodWithName_shouldSpawnChild() {
    TraceContext parent = tracer.newRootContext("parent");
    try (OpenContext ignored = tracer.open(parent)) {
      TraceContext result = dummyService.traceableDefaultWithName();

      assertThat(result).isNotNull();
      assertThat(result.getName()).isEqualTo("nameOfTraceableDefault");
      assertThat(result.getTraceId()).isNotNull();
      assertThat(result.getTraceId()).isEqualTo(parent.getTraceId());
      assertThat(result.getSpanId()).isNotNull();
      assertThat(result.getSpanId()).isNotEqualTo(parent.getSpanId());
      assertThat(result.getParentSpanId()).isEqualTo(parent.getSpanId());

      assertThat(tracer.getCurrentContext()).isEqualTo(parent);
    }
  }

  @Test
  void givenNoContext_whenCallingRequiresNewMethodWithoutName_shouldSpawnNewContext() {
    assertThat(tracer.getCurrentContext()).isNull();

    TraceContext result = dummyService.traceableRequiresNewWithoutName();

    assertThat(result).isNotNull();
    assertThat(result.getName()).isEqualTo("DummyService.traceableRequiresNewWithoutName");
    assertThat(result.getTraceId()).isNotNull();
    assertThat(result.getSpanId()).isNotNull();
    assertThat(result.getParentSpanId()).isNull();

    assertThat(tracer.getCurrentContext()).isNull();
  }

  @Test
  void givenNoContext_whenCallingRequiresNewMethodWithName_shouldSpawnNewContext() {
    assertThat(tracer.getCurrentContext()).isNull();

    TraceContext result = dummyService.traceableRequiresNewWithName();

    assertThat(result).isNotNull();
    assertThat(result.getName()).isEqualTo("nameOfTraceableRequiresNew");
    assertThat(result.getTraceId()).isNotNull();
    assertThat(result.getSpanId()).isNotNull();
    assertThat(result.getParentSpanId()).isNull();

    assertThat(tracer.getCurrentContext()).isNull();
  }

  @Test
  void givenOpenContext_whenCallingRequiresNewMethodWithoutName_shouldSpawnNewContext() {
    TraceContext parent = tracer.newRootContext("parent");
    try (OpenContext ignored = tracer.open(parent)) {
      TraceContext result = dummyService.traceableRequiresNewWithoutName();

      assertThat(result).isNotNull();
      assertThat(result.getName()).isEqualTo("DummyService.traceableRequiresNewWithoutName");
      assertThat(result.getTraceId()).isNotNull();
      assertThat(result.getTraceId()).isNotEqualTo(parent.getTraceId());
      assertThat(result.getSpanId()).isNotNull();
      assertThat(result.getSpanId()).isNotEqualTo(parent.getSpanId());
      assertThat(result.getParentSpanId()).isNull();

      assertThat(tracer.getCurrentContext()).isEqualTo(parent);
    }
  }

  @Test
  void givenOpenContext_whenCallingRequiresNewMethodWithName_shouldSpawnNewContext() {
    TraceContext parent = tracer.newRootContext("parent");
    try (OpenContext ignored = tracer.open(parent)) {
      TraceContext result = dummyService.traceableRequiresNewWithName();

      assertThat(result).isNotNull();
      assertThat(result.getName()).isEqualTo("nameOfTraceableRequiresNew");
      assertThat(result.getTraceId()).isNotNull();
      assertThat(result.getTraceId()).isNotEqualTo(parent.getTraceId());
      assertThat(result.getSpanId()).isNotNull();
      assertThat(result.getSpanId()).isNotEqualTo(parent.getSpanId());
      assertThat(result.getParentSpanId()).isNull();

      assertThat(tracer.getCurrentContext()).isEqualTo(parent);
    }
  }

  @Test
  void givenNoContext_whenCallingUntracedMethod_shouldNotSpawnContext() {
    assertThat(tracer.getCurrentContext()).isNull();

    TraceContext result = dummyService.untraced();
    assertThat(result).isNull();

    assertThat(tracer.getCurrentContext()).isNull();
  }

  @Test
  void givenOpenContext_whenCallingUntracedMethod_shouldReuseOpenContext() {
    TraceContext parent = tracer.newRootContext("parent");
    try (OpenContext ignored = tracer.open(parent)) {
      TraceContext result = dummyService.untraced();

      assertThat(result).isNotNull();
      assertThat(result).isSameAs(parent);

      assertThat(tracer.getCurrentContext()).isEqualTo(parent);
    }
  }

  @Service
  public static class DummyService {

    private final Tracer tracer;

    public DummyService(Tracer tracer) {
      this.tracer = tracer;
    }

    @Traceable
    public TraceContext traceableDefaultWithoutName() {
      return tracer.getCurrentContext();
    }

    @Traceable(name = "nameOfTraceableDefault")
    public TraceContext traceableDefaultWithName() {
      return tracer.getCurrentContext();
    }

    @Traceable(scope = TraceScope.REQUIRES_NEW)
    public TraceContext traceableRequiresNewWithoutName() {
      return tracer.getCurrentContext();
    }

    @Traceable(scope = TraceScope.REQUIRES_NEW, name = "nameOfTraceableRequiresNew")
    public TraceContext traceableRequiresNewWithName() {
      return tracer.getCurrentContext();
    }

    public TraceContext untraced() {
      return tracer.getCurrentContext();
    }
  }
}

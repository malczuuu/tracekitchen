package io.github.malczuuu.tracekitchen.spring.aspect;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.malczuuu.tracekitchen.OpenSpan;
import io.github.malczuuu.tracekitchen.Span;
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
    assertThat(tracer.getCurrentSpan()).isNull();

    Span result = dummyService.traceableDefaultWithoutName();

    assertThat(result).isNotNull();
    assertThat(result.getName()).isEqualTo("DummyService.traceableDefaultWithoutName");
    assertThat(result.getTrace().getTraceId()).isNotNull();
    assertThat(result.getTrace().getSpanId()).isNotNull();
    assertThat(result.getTrace().getParentSpanId()).isNull();

    assertThat(tracer.getCurrentSpan()).isNull();
  }

  @Test
  void givenNoContext_whenCallingTraceableMethodWithName_shouldSpawnNewContext() {
    assertThat(tracer.getCurrentSpan()).isNull();

    Span result = dummyService.traceableDefaultWithName();

    assertThat(result).isNotNull();
    assertThat(result.getName()).isEqualTo("nameOfTraceableDefault");
    assertThat(result.getTrace().getTraceId()).isNotNull();
    assertThat(result.getTrace().getSpanId()).isNotNull();
    assertThat(result.getTrace().getParentSpanId()).isNull();

    assertThat(tracer.getCurrentSpan()).isNull();
  }

  @Test
  void givenOpenContext_whenCallingTraceableMethodWithoutName_shouldSpawnChild() {
    Span parent = tracer.root("parent");
    try (OpenSpan ignored = parent.open()) {
      Span result = dummyService.traceableDefaultWithoutName();

      assertThat(result).isNotNull();
      assertThat(result.getName()).isEqualTo("DummyService.traceableDefaultWithoutName");
      assertThat(result.getTrace().getTraceId()).isNotNull();
      assertThat(result.getTrace().getTraceId()).isEqualTo(parent.getTrace().getTraceId());
      assertThat(result.getTrace().getSpanId()).isNotNull();
      assertThat(result.getTrace().getSpanId()).isNotEqualTo(parent.getTrace().getSpanId());
      assertThat(result.getTrace().getParentSpanId()).isEqualTo(parent.getTrace().getSpanId());

      assertThat(tracer.getCurrentSpan()).isEqualTo(parent);
    }
  }

  @Test
  void givenOpenContext_whenCallingTraceableMethodWithName_shouldSpawnChild() {
    Span parent = tracer.root("parent");
    try (OpenSpan ignored = parent.open()) {
      Span result = dummyService.traceableDefaultWithName();

      assertThat(result).isNotNull();
      assertThat(result.getName()).isEqualTo("nameOfTraceableDefault");
      assertThat(result.getTrace().getTraceId()).isNotNull();
      assertThat(result.getTrace().getTraceId()).isEqualTo(parent.getTrace().getTraceId());
      assertThat(result.getTrace().getSpanId()).isNotNull();
      assertThat(result.getTrace().getSpanId()).isNotEqualTo(parent.getTrace().getSpanId());
      assertThat(result.getTrace().getParentSpanId()).isEqualTo(parent.getTrace().getSpanId());

      assertThat(tracer.getCurrentSpan()).isEqualTo(parent);
    }
  }

  @Test
  void givenNoContext_whenCallingRequiresNewMethodWithoutName_shouldSpawnNewContext() {
    assertThat(tracer.getCurrentSpan()).isNull();

    Span result = dummyService.traceableRequiresNewWithoutName();

    assertThat(result).isNotNull();
    assertThat(result.getName()).isEqualTo("DummyService.traceableRequiresNewWithoutName");
    assertThat(result.getTrace().getTraceId()).isNotNull();
    assertThat(result.getTrace().getSpanId()).isNotNull();
    assertThat(result.getTrace().getParentSpanId()).isNull();

    assertThat(tracer.getCurrentSpan()).isNull();
  }

  @Test
  void givenNoContext_whenCallingRequiresNewMethodWithName_shouldSpawnNewContext() {
    assertThat(tracer.getCurrentSpan()).isNull();

    Span result = dummyService.traceableRequiresNewWithName();

    assertThat(result).isNotNull();
    assertThat(result.getName()).isEqualTo("nameOfTraceableRequiresNew");
    assertThat(result.getTrace().getTraceId()).isNotNull();
    assertThat(result.getTrace().getSpanId()).isNotNull();
    assertThat(result.getTrace().getParentSpanId()).isNull();

    assertThat(tracer.getCurrentSpan()).isNull();
  }

  @Test
  void givenOpenContext_whenCallingRequiresNewMethodWithoutName_shouldSpawnNewContext() {
    Span parent = tracer.root("parent");
    try (OpenSpan ignored = parent.open()) {
      Span result = dummyService.traceableRequiresNewWithoutName();

      assertThat(result).isNotNull();
      assertThat(result.getName()).isEqualTo("DummyService.traceableRequiresNewWithoutName");
      assertThat(result.getTrace().getTraceId()).isNotNull();
      assertThat(result.getTrace().getTraceId()).isNotEqualTo(parent.getTrace().getTraceId());
      assertThat(result.getTrace().getSpanId()).isNotNull();
      assertThat(result.getTrace().getSpanId()).isNotEqualTo(parent.getTrace().getSpanId());
      assertThat(result.getTrace().getParentSpanId()).isNull();

      assertThat(tracer.getCurrentSpan()).isEqualTo(parent);
    }
  }

  @Test
  void givenOpenContext_whenCallingRequiresNewMethodWithName_shouldSpawnNewContext() {
    Span parent = tracer.root("parent");
    try (OpenSpan ignored = parent.open()) {
      Span result = dummyService.traceableRequiresNewWithName();

      assertThat(result).isNotNull();
      assertThat(result.getName()).isEqualTo("nameOfTraceableRequiresNew");
      assertThat(result.getTrace().getTraceId()).isNotNull();
      assertThat(result.getTrace().getTraceId()).isNotEqualTo(parent.getTrace().getTraceId());
      assertThat(result.getTrace().getSpanId()).isNotNull();
      assertThat(result.getTrace().getSpanId()).isNotEqualTo(parent.getTrace().getSpanId());
      assertThat(result.getTrace().getParentSpanId()).isNull();

      assertThat(tracer.getCurrentSpan()).isEqualTo(parent);
    }
  }

  @Test
  void givenNoContext_whenCallingUntracedMethod_shouldNotSpawnContext() {
    assertThat(tracer.getCurrentSpan()).isNull();

    Span result = dummyService.untraced();
    assertThat(result).isNull();

    assertThat(tracer.getCurrentSpan()).isNull();
  }

  @Test
  void givenOpenContext_whenCallingUntracedMethod_shouldReuseOpenContext() {
    Span parent = tracer.root("parent");
    try (OpenSpan ignored = parent.open()) {
      Span result = dummyService.untraced();

      assertThat(result).isNotNull();
      assertThat(result).isSameAs(parent);

      assertThat(tracer.getCurrentSpan()).isEqualTo(parent);
    }
  }

  @Service
  public static class DummyService {

    private final Tracer tracer;

    public DummyService(Tracer tracer) {
      this.tracer = tracer;
    }

    @Traceable
    public Span traceableDefaultWithoutName() {
      return tracer.getCurrentSpan();
    }

    @Traceable(name = "nameOfTraceableDefault")
    public Span traceableDefaultWithName() {
      return tracer.getCurrentSpan();
    }

    @Traceable(scope = TraceScope.REQUIRES_NEW)
    public Span traceableRequiresNewWithoutName() {
      return tracer.getCurrentSpan();
    }

    @Traceable(scope = TraceScope.REQUIRES_NEW, name = "nameOfTraceableRequiresNew")
    public Span traceableRequiresNewWithName() {
      return tracer.getCurrentSpan();
    }

    public Span untraced() {
      return tracer.getCurrentSpan();
    }
  }
}

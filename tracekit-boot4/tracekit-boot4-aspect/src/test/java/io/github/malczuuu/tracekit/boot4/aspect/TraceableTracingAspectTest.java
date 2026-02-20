/*
 * Copyright (c) 2026 Damian Malczewski
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.github.malczuuu.tracekit.boot4.aspect;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.malczuuu.tracekit.OpenSpan;
import io.github.malczuuu.tracekit.Span;
import io.github.malczuuu.tracekit.TraceScope;
import io.github.malczuuu.tracekit.Traceable;
import io.github.malczuuu.tracekit.Tracer;
import io.github.malczuuu.tracekit.boot4.aspect.autoconfigure.TracekitAspectAutoConfiguration;
import io.github.malczuuu.tracekit.boot4.autoconfigure.TracekitAutoConfiguration;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(profiles = {"test"})
@EnableAspectJAutoProxy
@NullUnmarked
@SpringBootTest(
    classes = {
      TracekitAutoConfiguration.class,
      TracekitAspectAutoConfiguration.class,
      TraceableTracingAspectTest.DummyService.class
    })
class TraceableTracingAspectTest {

  @Autowired private DummyService dummyService;

  @Autowired private Tracer tracer;

  @Test
  void givenNoContext_whenCallingTraceableMethodWithoutName_shouldSpawnNewContext() {
    assertThat(tracer.findCurrentSpan()).isEmpty();

    Span result = dummyService.traceableDefaultWithoutName();

    assertThat(result).isNotNull();
    assertThat(result.getName()).isEqualTo("DummyService.traceableDefaultWithoutName");
    assertThat(result.getTrace().getTraceId()).isNotNull();
    assertThat(result.getTrace().getSpanId()).isNotNull();
    assertThat(result.getTrace().getParentSpanId()).isNull();

    assertThat(tracer.findCurrentSpan()).isEmpty();
  }

  @Test
  void givenNoContext_whenCallingTraceableMethodWithName_shouldSpawnNewContext() {
    assertThat(tracer.findCurrentSpan()).isEmpty();

    Span result = dummyService.traceableDefaultWithName();

    assertThat(result).isNotNull();
    assertThat(result.getName()).isEqualTo("nameOfTraceableDefault");
    assertThat(result.getTrace().getTraceId()).isNotNull();
    assertThat(result.getTrace().getSpanId()).isNotNull();
    assertThat(result.getTrace().getParentSpanId()).isNull();

    assertThat(tracer.findCurrentSpan()).isEmpty();
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
    assertThat(tracer.findCurrentSpan()).isEmpty();

    Span result = dummyService.traceableRequiresNewWithoutName();

    assertThat(result).isNotNull();
    assertThat(result.getName()).isEqualTo("DummyService.traceableRequiresNewWithoutName");
    assertThat(result.getTrace().getTraceId()).isNotNull();
    assertThat(result.getTrace().getSpanId()).isNotNull();
    assertThat(result.getTrace().getParentSpanId()).isNull();

    assertThat(tracer.findCurrentSpan()).isEmpty();
  }

  @Test
  void givenNoContext_whenCallingRequiresNewMethodWithName_shouldSpawnNewContext() {
    assertThat(tracer.findCurrentSpan()).isEmpty();

    Span result = dummyService.traceableRequiresNewWithName();

    assertThat(result).isNotNull();
    assertThat(result.getName()).isEqualTo("nameOfTraceableRequiresNew");
    assertThat(result.getTrace().getTraceId()).isNotNull();
    assertThat(result.getTrace().getSpanId()).isNotNull();
    assertThat(result.getTrace().getParentSpanId()).isNull();

    assertThat(tracer.findCurrentSpan()).isEmpty();
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
    assertThat(tracer.findCurrentSpan()).isEmpty();

    Span result = dummyService.untraced();
    assertThat(result).isNull();

    assertThat(tracer.findCurrentSpan()).isEmpty();
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
      return tracer.findCurrentSpan().orElse(null);
    }
  }
}

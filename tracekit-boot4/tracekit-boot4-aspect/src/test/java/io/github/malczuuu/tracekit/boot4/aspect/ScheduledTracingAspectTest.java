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
import io.github.malczuuu.tracekit.Tracer;
import io.github.malczuuu.tracekit.boot4.aspect.autoconfigure.TracekitAspectAutoConfiguration;
import io.github.malczuuu.tracekit.boot4.autoconfigure.TracekitAutoConfiguration;
import org.jspecify.annotations.NullUnmarked;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(profiles = {"test"})
@EnableAspectJAutoProxy
@NullUnmarked
@SpringBootTest(
    classes = {
      TracekitAutoConfiguration.class,
      TracekitAspectAutoConfiguration.class,
      ScheduledTracingAspectTest.DummyScheduledService.class
    })
class ScheduledTracingAspectTest {

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

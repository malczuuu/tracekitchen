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

package io.github.malczuuu.tracekit.simple;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.malczuuu.tracekit.Tracer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TracingScheduledExecutorTest {

  private Tracer tracer;
  private ScheduledExecutorService scheduler;

  @BeforeEach
  void beforeEach() {
    tracer = SimpleTracer.create();
    scheduler = tracer.wrap(Executors.newScheduledThreadPool(2));
  }

  @AfterEach
  void afterEach() {
    scheduler.shutdownNow();
  }

  @Test
  void givenScheduledRunnable_whenExecuted_thenNewRootSpanIsCreated() throws Exception {
    AtomicReference<String> capturedTraceId = new AtomicReference<>();
    CountDownLatch latch = new CountDownLatch(1);

    scheduler.schedule(
        () -> {
          capturedTraceId.set(tracer.getCurrentSpan().getTrace().getTraceId());
          latch.countDown();
        },
        1,
        TimeUnit.MILLISECONDS);

    assertThat(latch.await(5, TimeUnit.SECONDS)).isTrue();
    assertThat(capturedTraceId.get()).isNotNull();
  }

  @Test
  void givenScheduledCallable_whenExecuted_thenNewRootSpanIsCreated() throws Exception {
    Future<String> future =
        scheduler.schedule(
            () -> tracer.getCurrentSpan().getTrace().getTraceId(), 1, TimeUnit.MILLISECONDS);

    String traceId = future.get(5, TimeUnit.SECONDS);
    assertThat(traceId).isNotNull();
  }

  @Test
  void givenScheduledRunnable_whenExecuted_thenSpanHasNoParent() throws Exception {
    AtomicReference<String> capturedParentSpanId = new AtomicReference<>("sentinel");
    CountDownLatch latch = new CountDownLatch(1);

    scheduler.schedule(
        () -> {
          capturedParentSpanId.set(
              String.valueOf(tracer.getCurrentSpan().getTrace().getParentSpanId()));
          latch.countDown();
        },
        1,
        TimeUnit.MILLISECONDS);

    assertThat(latch.await(5, TimeUnit.SECONDS)).isTrue();
    assertThat(capturedParentSpanId.get()).isEqualTo("null");
  }

  @Test
  void givenRepeatedExecution_whenScheduledAtFixedRate_thenEachExecutionHasDistinctTrace()
      throws Exception {
    String[] traceIds = new String[2];
    AtomicInteger counter = new AtomicInteger(0);
    CountDownLatch latch = new CountDownLatch(2);

    scheduler.scheduleAtFixedRate(
        () -> {
          int index = counter.getAndIncrement();
          if (index < 2) {
            traceIds[index] = tracer.getCurrentSpan().getTrace().getTraceId();
            latch.countDown();
          }
        },
        0,
        1,
        TimeUnit.MILLISECONDS);

    assertThat(latch.await(5, TimeUnit.SECONDS)).isTrue();
    assertThat(traceIds[0]).isNotNull();
    assertThat(traceIds[1]).isNotNull();
    assertThat(traceIds[0]).isNotEqualTo(traceIds[1]);
  }

  @Test
  void givenRepeatedExecution_whenScheduledWithFixedDelay_thenEachExecutionHasDistinctTrace()
      throws Exception {
    String[] traceIds = new String[2];
    AtomicInteger counter = new AtomicInteger(0);
    CountDownLatch latch = new CountDownLatch(2);

    scheduler.scheduleWithFixedDelay(
        () -> {
          int index = counter.getAndIncrement();
          if (index < 2) {
            traceIds[index] = tracer.getCurrentSpan().getTrace().getTraceId();
            latch.countDown();
          }
        },
        0,
        1,
        TimeUnit.MILLISECONDS);

    assertThat(latch.await(5, TimeUnit.SECONDS)).isTrue();
    assertThat(traceIds[0]).isNotNull();
    assertThat(traceIds[1]).isNotNull();
    assertThat(traceIds[0]).isNotEqualTo(traceIds[1]);
  }

  @Test
  void givenSubmittedCallable_whenExecuted_thenNewRootSpanIsCreated() throws Exception {
    Future<String> future = scheduler.submit(() -> tracer.getCurrentSpan().getTrace().getTraceId());

    String traceId = future.get(5, TimeUnit.SECONDS);
    assertThat(traceId).isNotNull();
  }

  @Test
  void lifecycleMethodsDelegateCorrectly() throws Exception {
    assertThat(scheduler.isShutdown()).isFalse();
    assertThat(scheduler.isTerminated()).isFalse();

    scheduler.shutdown();

    assertThat(scheduler.isShutdown()).isTrue();
    assertThat(scheduler.awaitTermination(5, TimeUnit.SECONDS)).isTrue();
    assertThat(scheduler.isTerminated()).isTrue();
  }
}

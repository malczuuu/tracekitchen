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

import io.github.malczuuu.tracekit.OpenSpan;
import io.github.malczuuu.tracekit.Span;
import io.github.malczuuu.tracekit.Tracer;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TracingExecutorTest {

  private Tracer tracer;
  private ExecutorService tracingExecutor;

  @BeforeEach
  void beforeEach() {
    tracer = SimpleTracer.create();
    tracingExecutor = tracer.wrap(Executors.newFixedThreadPool(2));
  }

  @AfterEach
  void afterEach() {
    tracingExecutor.shutdownNow();
  }

  @Test
  void givenActiveSpan_whenExecutingRunnable_thenContextIsPropagated() throws Exception {
    Span root = tracer.root("root");

    AtomicReference<String> capturedTraceId = new AtomicReference<>();
    CountDownLatch latch = new CountDownLatch(1);

    try (OpenSpan os = root.open()) {
      tracingExecutor.execute(
          () -> {
            capturedTraceId.set(tracer.getCurrentSpan().getTrace().getTraceId());
            latch.countDown();
          });
    }

    assertThat(latch.await(5, TimeUnit.SECONDS)).isTrue();

    assertThat(capturedTraceId.get()).isEqualTo(root.getTrace().getTraceId());
  }

  @Test
  void givenActiveSpan_whenSubmittingRunnable_thenContextIsPropagated() throws Exception {
    Span root = tracer.root("root");

    AtomicReference<String> capturedTraceId = new AtomicReference<>();
    CountDownLatch latch = new CountDownLatch(1);

    try (OpenSpan os = root.open()) {
      tracingExecutor.submit(
          (Runnable)
              () -> {
                capturedTraceId.set(tracer.getCurrentSpan().getTrace().getTraceId());
                latch.countDown();
              });
    }

    assertThat(latch.await(5, TimeUnit.SECONDS)).isTrue();

    assertThat(capturedTraceId.get()).isEqualTo(root.getTrace().getTraceId());
  }

  @Test
  void givenActiveSpan_whenSubmittingCallable_thenContextIsPropagated() throws Exception {
    Span root = tracer.root("root");

    Future<String> future;
    try (OpenSpan os = root.open()) {
      future = tracingExecutor.submit(() -> tracer.getCurrentSpan().getTrace().getTraceId());
    }

    assertThat(future.get(5, TimeUnit.SECONDS)).isEqualTo(root.getTrace().getTraceId());
  }

  @Test
  void givenActiveSpan_whenInvokingAll_thenAllTasksHaveContext() throws Exception {
    Span root = tracer.root("root");

    List<Future<String>> futures;
    try (OpenSpan os = root.open()) {
      List<Callable<String>> tasks =
          Arrays.asList(
              () -> tracer.getCurrentSpan().getTrace().getTraceId(),
              () -> tracer.getCurrentSpan().getTrace().getTraceId());

      futures = tracingExecutor.invokeAll(tasks);
    }

    for (Future<String> future : futures) {
      assertThat(future.get(5, TimeUnit.SECONDS)).isEqualTo(root.getTrace().getTraceId());
    }
  }

  @Test
  void givenNoActiveSpan_whenSubmittingCallable_thenNoSpanIsAvailable() throws Exception {
    Future<Boolean> future = tracingExecutor.submit(() -> tracer.findCurrentSpan().isPresent());

    assertThat(future.get(5, TimeUnit.SECONDS)).isFalse();
  }

  @Test
  void givenActiveSpan_whenSubmitting_thenChildSpanHasCorrectParent() throws Exception {
    Span root = tracer.root("root");

    Future<String> future;
    try (OpenSpan os = root.open()) {
      future = tracingExecutor.submit(() -> tracer.getCurrentSpan().getTrace().getParentSpanId());
    }

    assertThat(future.get(5, TimeUnit.SECONDS)).isEqualTo(root.getTrace().getSpanId());
  }

  @Test
  void givenActiveSpan_whenSubmitting_thenChildSpanHasDistinctSpanId() throws Exception {
    Span root = tracer.root("root");

    Future<String> future;
    try (OpenSpan os = root.open()) {
      future = tracingExecutor.submit(() -> tracer.getCurrentSpan().getTrace().getSpanId());
    }

    assertThat(future.get(5, TimeUnit.SECONDS)).isNotEqualTo(root.getTrace().getSpanId());
  }

  @Test
  void lifecycleMethodsDelegateCorrectly() throws Exception {
    assertThat(tracingExecutor.isShutdown()).isFalse();
    assertThat(tracingExecutor.isTerminated()).isFalse();

    tracingExecutor.shutdown();

    assertThat(tracingExecutor.isShutdown()).isTrue();
    assertThat(tracingExecutor.awaitTermination(5, TimeUnit.SECONDS)).isTrue();
    assertThat(tracingExecutor.isTerminated()).isTrue();
  }
}

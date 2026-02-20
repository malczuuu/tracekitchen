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

import io.github.malczuuu.tracekit.OpenSpan;
import io.github.malczuuu.tracekit.Span;
import io.github.malczuuu.tracekit.Tracer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * An {@link ExecutorService} decorator that automatically propagates trace context to all submitted
 * tasks.
 *
 * <p>Every {@link Runnable} and {@link Callable} submitted through this executor is wrapped so that
 * the span active at submission time is propagated as a parent to the executing thread.
 */
final class TracingExecutor implements ExecutorService {

  private final ExecutorService delegate;
  private final Tracer tracer;

  TracingExecutor(ExecutorService delegate, Tracer tracer) {
    this.delegate = delegate;
    this.tracer = tracer;
  }

  @Override
  public void execute(Runnable command) {
    delegate.execute(wrap(command));
  }

  @Override
  public Future<?> submit(Runnable task) {
    return delegate.submit(wrap(task));
  }

  @Override
  public <T> Future<T> submit(Callable<T> task) {
    return delegate.submit(wrap(task));
  }

  @Override
  public <T> Future<T> submit(Runnable task, T result) {
    return delegate.submit(wrap(task), result);
  }

  @Override
  public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
      throws InterruptedException {
    return delegate.invokeAll(wrapAll(tasks));
  }

  @Override
  public <T> List<Future<T>> invokeAll(
      Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
      throws InterruptedException {
    return delegate.invokeAll(wrapAll(tasks), timeout, unit);
  }

  @Override
  public <T> T invokeAny(Collection<? extends Callable<T>> tasks)
      throws InterruptedException, ExecutionException {
    return delegate.invokeAny(wrapAll(tasks));
  }

  @Override
  public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
      throws InterruptedException, ExecutionException, TimeoutException {
    return delegate.invokeAny(wrapAll(tasks), timeout, unit);
  }

  @Override
  public void shutdown() {
    delegate.shutdown();
  }

  @Override
  public List<Runnable> shutdownNow() {
    return delegate.shutdownNow();
  }

  @Override
  public boolean isShutdown() {
    return delegate.isShutdown();
  }

  @Override
  public boolean isTerminated() {
    return delegate.isTerminated();
  }

  @Override
  public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
    return delegate.awaitTermination(timeout, unit);
  }

  private Runnable wrap(Runnable task) {
    return tracer
        .findCurrentSpan()
        .map(current -> (Runnable) () -> run(task, current))
        .orElse(task);
  }

  private <T> Callable<T> wrap(Callable<T> task) {
    return tracer
        .findCurrentSpan()
        .map(current -> (Callable<T>) () -> call(task, current))
        .orElse(task);
  }

  private <T> List<Callable<T>> wrapAll(Collection<? extends Callable<T>> tasks) {
    return tracer
        .findCurrentSpan()
        .map(
            current -> {
              List<Callable<T>> wrapped = new ArrayList<>(tasks.size());
              for (Callable<T> task : tasks) {
                wrapped.add(() -> call(task, current));
              }
              return wrapped;
            })
        .orElse(new ArrayList<>(tasks));
  }

  private void run(Runnable task, Span current) {
    Span child = current.spawnChild();
    try (OpenSpan ignored = child.open()) {
      task.run();
    }
  }

  private <T> T call(Callable<T> task, Span current) throws Exception {
    Span child = current.spawnChild();
    try (OpenSpan ignored = child.open()) {
      return task.call();
    }
  }
}

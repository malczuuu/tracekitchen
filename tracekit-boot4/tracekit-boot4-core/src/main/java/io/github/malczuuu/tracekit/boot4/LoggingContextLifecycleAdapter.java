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
package io.github.malczuuu.tracekit.boot4;

import io.github.malczuuu.tracekit.Span;
import io.github.malczuuu.tracekit.SpanLifecycleAdapter;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * {@code LoggingContextLifecycleAdapter} is an implementation of {@link SpanLifecycleAdapter} that
 * synchronizes the currently active span information with {@link MDC} and logs span lifecycle
 * events.
 *
 * <p>Main responsibilities:
 *
 * <ul>
 *   <li>Sets {@code traceId}, {@code spanId}, and optionally {@code parentSpanId} in MDC when a
 *       span is opened.
 *   <li>Logs debug messages when a span is opened or closed.
 *   <li>Upon span closure, restores the previous span in MDC or clears MDC trace entries if no span
 *       remains active.
 * </ul>
 *
 * <p>This allows trace information to be propagated into logs for proper correlation.
 */
public class LoggingContextLifecycleAdapter implements SpanLifecycleAdapter {

  private static final Logger log = LoggerFactory.getLogger(LoggingContextLifecycleAdapter.class);

  /**
   * Called after a span is opened. Sets the span identifiers in MDC and logs debug information
   * about the opened span.
   *
   * @param span the span that was just opened
   * @param previousSpan the previously active span, or {@code null} if none existed
   */
  @Override
  public void afterOpened(Span span, @Nullable Span previousSpan) {
    set(span);
    log.debug("Span [{}] opened", span.getName());
  }

  /**
   * Called after a span is closed. Logs debug information including the span name and its duration.
   * Then updates MDC with the currently active span or clears trace entries if no span remains
   * active.
   *
   * @param span the span that was just closed
   * @param currentSpan the currently active span after closure, or {@code null} if none
   */
  @Override
  public void afterClosed(Span span, @Nullable Span currentSpan) {
    log.debug("Span [{}] closed; durationMilli={}", span.getName(), span.getDuration().toMillis());
    if (currentSpan != null) {
      set(currentSpan);
    } else {
      drop();
    }
  }

  private void set(Span current) {
    MDC.put("traceId", current.getTrace().getTraceId());
    MDC.put("spanId", current.getTrace().getSpanId());
    if (current.getTrace().getParentSpanId() != null) {
      MDC.put("parentSpanId", current.getTrace().getParentSpanId());
    } else {
      MDC.remove("parentSpanId");
    }
  }

  private void drop() {
    MDC.remove("traceId");
    MDC.remove("spanId");
    MDC.remove("parentSpanId");
  }
}

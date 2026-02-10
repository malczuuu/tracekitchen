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
package io.github.malczuuu.tracekit.boot4.webmvc;

import io.github.malczuuu.tracekit.Span;
import io.github.malczuuu.tracekit.SpanBuilder;
import io.github.malczuuu.tracekit.Tracer;
import io.github.malczuuu.tracekit.boot4.TraceHeaderSettings;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

/**
 * Extracts tracing information from HTTP requests and builds {@link Span} instances.
 *
 * <p>Typical usage is within a servlet filter or interceptor to reconstruct trace context from
 * incoming requests.
 */
public class ServletRequestExtractor {

  private final Tracer tracer;
  private final TraceHeaderSettings settings;

  /**
   * Creates a new {@code ServletRequestExtractor} with the given tracer and header settings.
   *
   * @param tracer the {@link Tracer} used to build new {@link Span} instances
   * @param settings the header configuration defining which HTTP headers carry trace information
   */
  public ServletRequestExtractor(Tracer tracer, TraceHeaderSettings settings) {
    this.tracer = tracer;
    this.settings = settings;
  }

  /**
   * Extracts a {@link Span} from the given HTTP request.
   *
   * <p>If the request does not contain sufficient information (traceId, spanId), an empty {@link
   * Optional} is returned.
   *
   * @param origin the HTTP request to extract tracing information from
   * @return an {@link Optional} containing the built {@link Span}, or empty if incomplete
   */
  public Optional<Span> extract(HttpServletRequest origin) {
    SpanBuilder builder = tracer.builder();

    builder = appendTraceId(origin, builder);
    builder = appendSpanId(origin, builder);
    builder = appendParentSpanId(origin, builder);

    if (!builder.isComplete()) {
      return Optional.empty();
    }

    return Optional.of(builder.build());
  }

  /**
   * Appends the trace ID to the {@link SpanBuilder} if a configured header is present.
   *
   * @param origin the HTTP request
   * @param builder the {@link SpanBuilder} to append to
   * @return the updated {@link SpanBuilder}
   */
  protected SpanBuilder appendTraceId(HttpServletRequest origin, SpanBuilder builder) {
    Optional<String> optionalValue = findHeader(origin, settings.getTraceIdHeaderNames());
    if (optionalValue.isPresent()) {
      builder = builder.withTraceId(optionalValue.get());
    }
    return builder;
  }

  /**
   * Appends the span ID to the {@link SpanBuilder} if a configured header is present.
   *
   * @param origin the HTTP request
   * @param builder the {@link SpanBuilder} to append to
   * @return the updated {@link SpanBuilder}
   */
  protected SpanBuilder appendSpanId(HttpServletRequest origin, SpanBuilder builder) {
    Optional<String> optionalValue = findHeader(origin, settings.getSpanIdHeaderNames());
    if (optionalValue.isPresent()) {
      builder = builder.withSpanId(optionalValue.get());
    }
    return builder;
  }

  /**
   * Appends the parent span ID to the {@link SpanBuilder} if a configured header is present.
   *
   * @param origin the HTTP request
   * @param builder the {@link SpanBuilder} to append to
   * @return the updated {@link SpanBuilder}
   */
  protected SpanBuilder appendParentSpanId(HttpServletRequest origin, SpanBuilder builder) {
    Optional<String> optionalValue = findHeader(origin, settings.getParentIdHeaderNames());
    if (optionalValue.isPresent()) {
      builder = builder.withParentSpanId(optionalValue.get());
    }
    return builder;
  }

  /**
   * Finds the first non-null header value from the list of possible header names.
   *
   * <p>Headers are checked in the order given by {@code headerNames}. The first matching header
   * with a non-null value is returned.
   *
   * @param request the HTTP request
   * @param headerNames the list of header names to check
   * @return an {@link Optional} containing the header value, or empty if none found
   */
  protected Optional<String> findHeader(HttpServletRequest request, List<String> headerNames) {
    for (String headerName : headerNames) {
      String headerValue = request.getHeader(headerName);
      if (headerValue != null) {
        return Optional.of(headerValue);
      }
    }
    return Optional.empty();
  }
}

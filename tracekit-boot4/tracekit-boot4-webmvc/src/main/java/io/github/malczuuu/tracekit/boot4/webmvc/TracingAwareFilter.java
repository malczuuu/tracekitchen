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

import io.github.malczuuu.tracekit.OpenSpan;
import io.github.malczuuu.tracekit.Span;
import io.github.malczuuu.tracekit.Tracer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * A servlet filter that integrates tracing into the HTTP request lifecycle.
 *
 * <p>This filter extracts an existing trace context from incoming requests using {@link
 * ServletRequestExtractor}. If a context is found, it spawns a child {@link Span} for the current
 * request. Otherwise, it creates a new root {@link Span}.
 *
 * <p>The span is automatically opened before the request is processed and closed after the request
 * completes. This ensures that all downstream processing (controllers, services, etc.) occurs
 * within the proper trace context.
 *
 * <p>The filter implements {@link Ordered} and is set to run with high precedence (order = {@link
 * #ORDER}) so that tracing spans are established early in the filter chain.
 *
 * <p>Typical usage is in Spring Web MVC or Spring Boot applications to automatically correlate logs
 * and traces with HTTP requests.
 */
public class TracingAwareFilter extends OncePerRequestFilter implements Ordered {

  /** Returns the order of this filter in the filter chain. */
  public static final int ORDER = Ordered.HIGHEST_PRECEDENCE + 100;

  private final ServletRequestExtractor servletRequestExtractor;
  private final Tracer tracer;

  /**
   * Creates a new {@code TracingAwareFilter} with the given {@link ServletRequestExtractor} and
   * {@link Tracer}.
   *
   * @param servletRequestExtractor extractor used to read existing trace context from HTTP requests
   * @param tracer the tracer used to create root or child spans
   */
  public TracingAwareFilter(ServletRequestExtractor servletRequestExtractor, Tracer tracer) {
    this.servletRequestExtractor = servletRequestExtractor;
    this.tracer = tracer;
  }

  /**
   * Extracts the trace context from the request (if any) and creates a child span or a root span.
   * The span is opened for the duration of the request, ensuring proper trace correlation across
   * controllers and services.
   *
   * @param request the incoming HTTP request
   * @param response the HTTP response
   * @param filterChain the filter chain
   * @throws ServletException if an error occurs during filtering
   * @throws IOException if an I/O error occurs during filtering
   */
  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String contextName = request.getMethod() + " " + request.getRequestURI();
    Span span =
        servletRequestExtractor
            .extract(request)
            .map(c -> c.spawnChild(contextName))
            .orElseGet(() -> tracer.root(contextName));
    try (OpenSpan o = span.open()) {
      filterChain.doFilter(request, response);
    }
  }

  /**
   * Returns the order of this filter in the filter chain.
   *
   * @return the order (HIGHEST_PRECEDENCE + 100)
   */
  @Override
  public int getOrder() {
    return ORDER;
  }
}

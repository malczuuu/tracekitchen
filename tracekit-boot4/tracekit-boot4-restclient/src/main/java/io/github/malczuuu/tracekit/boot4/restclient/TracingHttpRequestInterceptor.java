package io.github.malczuuu.tracekit.boot4.restclient;

import io.github.malczuuu.tracekit.Span;
import io.github.malczuuu.tracekit.Tracer;
import io.github.malczuuu.tracekit.boot4.TraceHeaderSettings;
import java.io.IOException;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

/**
 * {@link ClientHttpRequestInterceptor} that propagates the current tracing context to outgoing HTTP
 * requests.
 *
 * <p>If a {@link Span} is currently active, this interceptor attaches trace identifiers as HTTP
 * headers to the request, allowing downstream services to continue the trace. Header names are
 * resolved from {@link TraceHeaderSettings}. For each identifier type (trace ID, span ID, parent
 * span ID), only the first configured header name is used.
 *
 * <p>If no active span is present, the request is executed without modification.
 *
 * <p>This interceptor is intended to be used with Spring's {@code RestTemplate} or other clients
 * relying on {@link ClientHttpRequestInterceptor}.
 */
public class TracingHttpRequestInterceptor implements ClientHttpRequestInterceptor {

  private final Tracer tracer;
  private final TraceHeaderSettings settings;

  /**
   * Creates a new tracing HTTP request interceptor.
   *
   * @param tracer tracer used to obtain the currently active span
   * @param settings configuration defining which HTTP headers should be used to propagate tracing
   *     identifiers
   */
  public TracingHttpRequestInterceptor(Tracer tracer, TraceHeaderSettings settings) {
    this.tracer = tracer;
    this.settings = settings;
  }

  /**
   * Intercepts the outgoing HTTP request and attaches tracing headers if a span is currently
   * active.
   *
   * @param request the outgoing HTTP request
   * @param body the request body
   * @param execution the request execution
   * @return the HTTP response
   * @throws IOException in case of I/O errors during request execution
   */
  @Override
  public ClientHttpResponse intercept(
      HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
    Span span = tracer.getCurrentSpan();

    if (span != null) {
      attachContextHeaders(request, span);
    }

    return execution.execute(request, body);
  }

  /**
   * Attaches trace-related headers to the given request using values from the provided span.
   *
   * <p>Only the first header name from each configured header list is used.
   *
   * @param request the HTTP request to modify
   * @param span the active span whose context should be propagated
   */
  protected void attachContextHeaders(HttpRequest request, Span span) {
    if (!settings.getTraceIdHeaderNames().isEmpty()) {
      request
          .getHeaders()
          .add(settings.getTraceIdHeaderNames().get(0), span.getTrace().getTraceId());
    }
    if (!settings.getSpanIdHeaderNames().isEmpty()) {
      request.getHeaders().add(settings.getSpanIdHeaderNames().get(0), span.getTrace().getSpanId());
    }
    if (!settings.getParentIdHeaderNames().isEmpty()) {
      request
          .getHeaders()
          .add(settings.getParentIdHeaderNames().get(0), span.getTrace().getParentSpanId());
    }
  }
}

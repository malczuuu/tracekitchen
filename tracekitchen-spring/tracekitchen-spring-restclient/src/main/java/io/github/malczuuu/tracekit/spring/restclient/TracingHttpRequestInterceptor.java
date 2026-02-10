package io.github.malczuuu.tracekit.spring.restclient;

import io.github.malczuuu.tracekit.Span;
import io.github.malczuuu.tracekit.Tracer;
import io.github.malczuuu.tracekit.spring.TraceHeaderSettings;
import java.io.IOException;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class TracingHttpRequestInterceptor implements ClientHttpRequestInterceptor {

  private final Tracer tracer;
  private final TraceHeaderSettings settings;

  public TracingHttpRequestInterceptor(Tracer tracer, TraceHeaderSettings settings) {
    this.tracer = tracer;
    this.settings = settings;
  }

  @Override
  public ClientHttpResponse intercept(
      HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
    Span span = tracer.getCurrentSpan();

    if (span != null) {
      attachContextHeaders(request, span);
    }

    return execution.execute(request, body);
  }

  private void attachContextHeaders(HttpRequest request, Span span) {
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

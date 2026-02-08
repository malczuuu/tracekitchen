package io.github.malczuuu.tracekitchen.spring.restclient;

import io.github.malczuuu.tracekitchen.TraceContext;
import io.github.malczuuu.tracekitchen.Tracer;
import io.github.malczuuu.tracekitchen.spring.TraceHeaderSettings;
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
    TraceContext context = tracer.getCurrentContext();

    if (context != null) {
      attachContextHeaders(request, context);
    }

    return execution.execute(request, body);
  }

  private void attachContextHeaders(HttpRequest request, TraceContext context) {
    if (!settings.getTraceIdHeaderNames().isEmpty()) {
      request.getHeaders().add(settings.getTraceIdHeaderNames().get(0), context.getTraceId());
    }
    if (!settings.getSpanIdHeaderNames().isEmpty()) {
      request.getHeaders().add(settings.getSpanIdHeaderNames().get(0), context.getSpanId());
    }
  }
}

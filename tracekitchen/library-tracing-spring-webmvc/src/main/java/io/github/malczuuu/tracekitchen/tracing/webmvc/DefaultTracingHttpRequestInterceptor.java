package io.github.malczuuu.tracekitchen.tracing.webmvc;

import io.github.malczuuu.tracekitchen.tracing.core.TraceContext;
import io.github.malczuuu.tracekitchen.tracing.core.Tracer;
import java.io.IOException;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

public class DefaultTracingHttpRequestInterceptor implements TracingHttpRequestInterceptor {

  private final Tracer tracer;
  private final TraceHeaderSettings settings;

  public DefaultTracingHttpRequestInterceptor(Tracer tracer, TraceHeaderSettings settings) {
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

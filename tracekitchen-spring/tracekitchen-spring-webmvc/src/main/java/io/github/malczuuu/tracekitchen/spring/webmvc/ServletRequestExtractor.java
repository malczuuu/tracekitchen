package io.github.malczuuu.tracekitchen.spring.webmvc;

import io.github.malczuuu.tracekitchen.Span;
import io.github.malczuuu.tracekitchen.SpanBuilder;
import io.github.malczuuu.tracekitchen.TraceExtractor;
import io.github.malczuuu.tracekitchen.Tracer;
import io.github.malczuuu.tracekitchen.spring.TraceHeaderSettings;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public class ServletRequestExtractor implements TraceExtractor<HttpServletRequest> {

  private final Tracer tracer;
  private final TraceHeaderSettings settings;

  public ServletRequestExtractor(Tracer tracer, TraceHeaderSettings settings) {
    this.tracer = tracer;
    this.settings = settings;
  }

  @Override
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

  protected SpanBuilder appendTraceId(HttpServletRequest origin, SpanBuilder builder) {
    Optional<String> optionalValue = findHeader(origin, settings.getTraceIdHeaderNames());
    if (optionalValue.isPresent()) {
      builder = builder.withTraceId(optionalValue.get());
    }
    return builder;
  }

  protected SpanBuilder appendSpanId(HttpServletRequest origin, SpanBuilder builder) {
    Optional<String> optionalValue = findHeader(origin, settings.getSpanIdHeaderNames());
    if (optionalValue.isPresent()) {
      builder = builder.withSpanId(optionalValue.get());
    }
    return builder;
  }

  protected SpanBuilder appendParentSpanId(HttpServletRequest origin, SpanBuilder builder) {
    Optional<String> optionalValue = findHeader(origin, settings.getParentIdHeaderNames());
    if (optionalValue.isPresent()) {
      builder = builder.withParentSpanId(optionalValue.get());
    }
    return builder;
  }

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

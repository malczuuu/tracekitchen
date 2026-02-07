package io.github.malczuuu.tracekitchen.spring.webmvc;

import io.github.malczuuu.tracekitchen.TraceContext;
import io.github.malczuuu.tracekitchen.TraceContextBuilder;
import io.github.malczuuu.tracekitchen.Tracer;
import io.github.malczuuu.tracekitchen.spring.TraceHeaderSettings;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public class DefaultServletRequestExtractor implements ServletRequestExtractor {

  private final Tracer tracer;
  private final TraceHeaderSettings settings;

  public DefaultServletRequestExtractor(Tracer tracer, TraceHeaderSettings settings) {
    this.tracer = tracer;
    this.settings = settings;
  }

  @Override
  public TraceContext extract(HttpServletRequest origin) {
    TraceContextBuilder builder = tracer.createBuilder();

    builder = appendTraceId(origin, builder);
    builder = appendSpanId(origin, builder);
    builder = appendParentSpanId(origin, builder);

    return builder.build();
  }

  private TraceContextBuilder appendTraceId(
      HttpServletRequest origin, TraceContextBuilder builder) {
    Optional<String> optionalValue = findHeader(origin, settings.getTraceIdHeaderNames());
    if (optionalValue.isPresent()) {
      builder = builder.withTraceId(optionalValue.get());
    }
    return builder;
  }

  private TraceContextBuilder appendSpanId(HttpServletRequest origin, TraceContextBuilder builder) {
    Optional<String> optionalValue = findHeader(origin, settings.getSpanIdHeaderNames());
    if (optionalValue.isPresent()) {
      builder = builder.withSpanId(optionalValue.get());
    }
    return builder;
  }

  private TraceContextBuilder appendParentSpanId(
      HttpServletRequest origin, TraceContextBuilder builder) {
    Optional<String> optionalValue = findHeader(origin, settings.getParentIdHeaderNames());
    if (optionalValue.isPresent()) {
      builder = builder.withParentSpanId(optionalValue.get());
    }
    return builder;
  }

  private Optional<String> findHeader(HttpServletRequest request, List<String> headerNames) {
    for (String headerName : headerNames) {
      String headerValue = request.getHeader(headerName);
      if (headerValue != null) {
        return Optional.of(headerValue);
      }
    }
    return Optional.empty();
  }
}

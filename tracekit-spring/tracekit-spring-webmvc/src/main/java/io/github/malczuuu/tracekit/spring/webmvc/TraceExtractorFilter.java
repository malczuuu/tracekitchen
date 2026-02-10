package io.github.malczuuu.tracekit.spring.webmvc;

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

public class TraceExtractorFilter extends OncePerRequestFilter implements Ordered {

  public static final int ORDER = Ordered.HIGHEST_PRECEDENCE + 100;

  private final ServletRequestExtractor servletRequestExtractor;
  private final Tracer tracer;

  public TraceExtractorFilter(ServletRequestExtractor servletRequestExtractor, Tracer tracer) {
    this.servletRequestExtractor = servletRequestExtractor;
    this.tracer = tracer;
  }

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

  @Override
  public int getOrder() {
    return ORDER;
  }
}

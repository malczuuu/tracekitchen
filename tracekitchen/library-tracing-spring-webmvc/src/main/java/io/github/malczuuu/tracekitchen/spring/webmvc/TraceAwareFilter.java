package io.github.malczuuu.tracekitchen.spring.webmvc;

import io.github.malczuuu.tracekitchen.OpenContext;
import io.github.malczuuu.tracekitchen.TraceContext;
import io.github.malczuuu.tracekitchen.Tracer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.filter.OncePerRequestFilter;

public class TraceAwareFilter extends OncePerRequestFilter {

  private final ServletRequestExtractor servletRequestExtractor;
  private final Tracer tracer;

  public TraceAwareFilter(ServletRequestExtractor servletRequestExtractor, Tracer tracer) {
    this.servletRequestExtractor = servletRequestExtractor;
    this.tracer = tracer;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    TraceContext context = servletRequestExtractor.extract(request);
    try (OpenContext o = tracer.open(context)) {
      filterChain.doFilter(request, response);
    }
  }
}

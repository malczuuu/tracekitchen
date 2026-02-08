package io.github.malczuuu.tracekitchen.spring.webmvc;

import io.github.malczuuu.tracekitchen.OpenContext;
import io.github.malczuuu.tracekitchen.TraceContext;
import io.github.malczuuu.tracekitchen.Tracer;
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
    TraceContext context =
        servletRequestExtractor
            .extract(request)
            .map(c -> c.makeChild(contextName))
            .orElseGet(() -> tracer.newRootContext(contextName));
    try (OpenContext o = tracer.open(context)) {
      filterChain.doFilter(request, response);
    }
  }

  @Override
  public int getOrder() {
    return ORDER;
  }
}

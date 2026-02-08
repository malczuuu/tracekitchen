package io.github.malczuuu.tracekitchen.common;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Clock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

public class LoggingFilter extends OncePerRequestFilter implements Ordered {

  public static final int ORDER = Ordered.LOWEST_PRECEDENCE;

  private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

  private final Clock clock;

  public LoggingFilter(Clock clock) {
    this.clock = clock;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    long before = clock.millis();
    try {
      filterChain.doFilter(request, response);
    } finally {
      long elapsed = clock.millis() - before;
      log.info(
          "Processed HTTP request {} {} in {}ms",
          request.getMethod(),
          request.getRequestURI(),
          elapsed);
    }
  }

  @Override
  public int getOrder() {
    return ORDER;
  }
}

package io.github.malczuuu.tracekitchen.common.logging;

import io.github.problem4j.core.Problem;
import io.github.problem4j.core.ProblemContext;
import io.github.problem4j.spring.webmvc.AdviceWebMvcInspector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

public class LoggingAdviceInspector implements AdviceWebMvcInspector {

  private static final Logger log = LoggerFactory.getLogger(LoggingAdviceInspector.class);

  @Override
  public void inspect(
      ProblemContext context,
      Problem problem,
      Exception ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    String method = request instanceof ServletWebRequest s ? s.getRequest().getMethod() : "";
    String uri = request instanceof ServletWebRequest s ? s.getRequest().getRequestURI() : "";
    String call = String.join(" ", method, uri).strip();
    log.error("Handle error from RestController on {}; response={}", call, problem, ex);
  }
}

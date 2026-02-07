package io.github.malczuuu.tracekitchen.spring.webmvc;

import io.github.malczuuu.tracekitchen.TraceExtractor;
import jakarta.servlet.http.HttpServletRequest;

public interface ServletRequestExtractor extends TraceExtractor<HttpServletRequest> {}

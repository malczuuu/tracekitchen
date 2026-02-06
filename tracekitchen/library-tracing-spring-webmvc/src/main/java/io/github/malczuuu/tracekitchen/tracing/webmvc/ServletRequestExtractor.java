package io.github.malczuuu.tracekitchen.tracing.webmvc;

import io.github.malczuuu.tracekitchen.tracing.core.TraceExtractor;
import jakarta.servlet.http.HttpServletRequest;

public interface ServletRequestExtractor extends TraceExtractor<HttpServletRequest> {}

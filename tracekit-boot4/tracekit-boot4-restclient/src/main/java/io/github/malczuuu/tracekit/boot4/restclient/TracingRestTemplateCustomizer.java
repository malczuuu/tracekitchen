package io.github.malczuuu.tracekit.boot4.restclient;

import org.springframework.boot.restclient.RestTemplateCustomizer;

/**
 * Functional interface for customizing {@code RestTEmplate} with tracing capabilities.
 *
 * <p>This interface is a specialization of {@link RestTemplateCustomizer} and to be used as a
 * {@code @ConditionalOnMissingBean} target in Spring configuration. It allows applications to
 * provide a default or override implementation for injecting tracing behavior.
 *
 * @see org.springframework.web.client.RestTemplate
 * @see org.springframework.boot.restclient.RestTemplateBuilder
 */
@FunctionalInterface
public interface TracingRestTemplateCustomizer extends RestTemplateCustomizer {}

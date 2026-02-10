package io.github.malczuuu.tracekit.boot4.restclient;

import org.springframework.boot.restclient.RestClientCustomizer;

/**
 * Functional interface for customizing {@code RestCLient} with tracing capabilities.
 *
 * <p>This interface is a specialization of {@link RestClientCustomizer} and to be used as a
 * {@code @ConditionalOnMissingBean} target in Spring configuration. It allows applications to
 * provide a default or override implementation for injecting tracing behavior.
 *
 * @see org.springframework.web.client.RestClient
 * @see org.springframework.web.client.RestClient.Builder
 */
@FunctionalInterface
public interface TracingRestClientCustomizer extends RestClientCustomizer {}

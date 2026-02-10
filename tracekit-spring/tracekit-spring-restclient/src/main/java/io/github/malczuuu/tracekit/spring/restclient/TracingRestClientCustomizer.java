package io.github.malczuuu.tracekit.spring.restclient;

import org.springframework.boot.restclient.RestClientCustomizer;

@FunctionalInterface
public interface TracingRestClientCustomizer extends RestClientCustomizer {}

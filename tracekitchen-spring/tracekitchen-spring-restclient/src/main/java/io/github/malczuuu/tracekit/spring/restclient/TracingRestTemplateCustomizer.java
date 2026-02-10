package io.github.malczuuu.tracekit.spring.restclient;

import org.springframework.boot.restclient.RestTemplateCustomizer;

@FunctionalInterface
public interface TracingRestTemplateCustomizer extends RestTemplateCustomizer {}

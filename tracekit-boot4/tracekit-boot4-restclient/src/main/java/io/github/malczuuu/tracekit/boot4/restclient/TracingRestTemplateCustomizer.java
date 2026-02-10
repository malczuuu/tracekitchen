/*
 * Copyright (c) 2026 Damian Malczewski
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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

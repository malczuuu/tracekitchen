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

package io.github.malczuuu.tracekit.boot4;

/**
 * Functional interface alias for {@link SimpleTracerBuilderCustomizer} intended for use with
 * Spring's {@code ConditionalOnMissingBean}.
 *
 * <p>Marking a bean with this type allows conditional registration of a {@link
 * SimpleTracerBuilderCustomizer} only if no other bean of the same type is already present in the
 * Spring context.
 *
 * <p>Being a functional interface, it can be implemented using a lambda or method reference.
 *
 * @see org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
 */
@FunctionalInterface
public interface LoggingContextSimpleTracerBuilderCustomizer
    extends SimpleTracerBuilderCustomizer {}

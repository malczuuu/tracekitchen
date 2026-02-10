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

import java.util.List;

/**
 * Interface defining the HTTP header names used for propagating tracing information across service
 * boundaries.
 *
 * <p>Implementations provide the names of headers that carry trace identifiers for distributed
 * tracing, including the current trace ID, the current span ID, and the parent span ID.
 *
 * <p>This allows tracing libraries to extract and inject these identifiers consistently when making
 * or receiving HTTP calls.
 */
public interface TraceHeaderSettings {

  /**
   * Returns the list of header names that should be used to propagate the trace ID.
   *
   * @return a list of HTTP header names for the trace ID
   */
  List<String> getTraceIdHeaderNames();

  /**
   * Returns the list of header names that should be used to propagate the current span ID.
   *
   * @return a list of HTTP header names for the span ID
   */
  List<String> getSpanIdHeaderNames();

  /**
   * Returns the list of header names that should be used to propagate the parent span ID.
   *
   * @return a list of HTTP header names for the parent span ID
   */
  List<String> getParentIdHeaderNames();
}

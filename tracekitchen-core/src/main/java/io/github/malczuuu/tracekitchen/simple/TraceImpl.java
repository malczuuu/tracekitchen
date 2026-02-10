package io.github.malczuuu.tracekitchen.simple;

import io.github.malczuuu.tracekitchen.Trace;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

class TraceImpl implements Trace {

  private final String traceId;
  private final String spanId;
  private final @Nullable String parentSpanId;
  private final boolean sampled;

  TraceImpl(String traceId, String spanId) {
    this(traceId, spanId, null, false);
  }

  TraceImpl(String traceId, String spanId, @Nullable String parentSpanId, boolean sampled) {
    this.traceId = traceId;
    this.spanId = spanId;
    this.parentSpanId = parentSpanId;
    this.sampled = sampled;
  }

  @Override
  public String getTraceId() {
    return traceId;
  }

  @Override
  public String getSpanId() {
    return spanId;
  }

  @Override
  public @Nullable String getParentSpanId() {
    return parentSpanId;
  }

  @Override
  public boolean isSampled() {
    return sampled;
  }

  @Override
  public boolean isParentOf(@Nullable Trace trace) {
    if (trace == null) {
      return false;
    }
    return Objects.equals(traceId, trace.getTraceId())
        && Objects.equals(spanId, trace.getParentSpanId());
  }

  @Override
  public boolean isChildOf(@Nullable Trace trace) {
    if (trace == null) {
      return false;
    }
    return Objects.equals(traceId, trace.getTraceId())
        && Objects.equals(parentSpanId, trace.getSpanId());
  }

  @Override
  public Trace spawnChild(String spanId) {
    return new TraceImpl(traceId, spanId, this.spanId, sampled);
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Trace that)) {
      return false;
    }
    return Objects.equals(traceId, that.getTraceId())
        && Objects.equals(spanId, that.getSpanId())
        && Objects.equals(parentSpanId, that.getParentSpanId())
        && sampled == that.isSampled();
  }

  @Override
  public int hashCode() {
    return Objects.hash(traceId, spanId, parentSpanId, sampled);
  }

  @Override
  public String toString() {
    List<String> lines = new ArrayList<>();
    lines.add("traceId='" + traceId + "'");
    lines.add("spanId='" + spanId + "'");
    if (parentSpanId != null) {
      lines.add("parentSpanId='" + parentSpanId + "'");
    }
    lines.add("sampled=" + sampled);

    return "[" + String.join(", ", lines) + "]";
  }
}

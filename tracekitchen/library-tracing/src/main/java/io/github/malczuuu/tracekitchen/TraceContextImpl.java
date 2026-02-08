package io.github.malczuuu.tracekitchen;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.Nullable;

final class TraceContextImpl implements TraceContext {

  private final @Nullable String name;
  private final String traceId;
  private final String spanId;
  private final @Nullable String parentSpanId;

  private final TraceFactory traceFactory;

  private @Nullable Instant openedAt = null;
  private @Nullable Instant closedAt = null;

  TraceContextImpl(TraceFactory traceFactory) {
    this(null, traceFactory.makeTraceId(), traceFactory.makeSpanId(), null, traceFactory);
  }

  public TraceContextImpl(String name, TraceFactory traceFactory) {
    this(name, traceFactory.makeTraceId(), traceFactory.makeSpanId(), null, traceFactory);
  }

  TraceContextImpl(
      @Nullable String name,
      String traceId,
      String spanId,
      @Nullable String parentSpanId,
      TraceFactory traceFactory) {
    this.name = name;
    this.traceId = traceId;
    this.spanId = spanId;
    this.parentSpanId = parentSpanId;
    this.traceFactory = traceFactory;
  }

  @Override
  public TraceContext makeChild() {
    return new TraceContextImpl(null, traceId, traceFactory.makeSpanId(), spanId, traceFactory);
  }

  @Override
  public TraceContext makeChild(String name) {
    return new TraceContextImpl(name, traceId, traceFactory.makeSpanId(), spanId, traceFactory);
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
  public void open(Instant time) {
    if (closedAt != null) {
      throw new IllegalStateException("Cannot open a closed context");
    }
    if (openedAt != null) {
      throw new IllegalStateException("Cannot open an already opened context");
    }
    openedAt = time;
  }

  @Override
  public void close(Instant time) {
    if (openedAt == null) {
      throw new IllegalStateException("Cannot close a non-open context");
    }
    if (closedAt != null) {
      throw new IllegalStateException("Cannot close an already closed context");
    }
    if (time.isBefore(openedAt)) {
      throw new IllegalStateException("Cannot close context with time earlier than opening time");
    }
    closedAt = time;
  }

  @Override
  public ContextState getState() {
    if (closedAt != null) {
      return ContextState.CLOSED;
    }
    if (openedAt != null) {
      return ContextState.OPEN;
    }
    return ContextState.NEW;
  }

  @Override
  public Duration getDuration() {
    if (openedAt == null || closedAt == null) {
      return Duration.ZERO;
    }
    return Duration.between(openedAt, closedAt);
  }

  @Override
  public String toString() {
    List<String> lines = new ArrayList<>();
    if (name != null) {
      lines.add("name='" + name + "'");
    }

    lines.add("traceId='" + traceId + "'");
    lines.add("spanId='" + spanId + "'");

    if (parentSpanId != null) {
      lines.add("parentSpanId='" + parentSpanId + "'");
    }
    if (openedAt != null) {
      lines.add("openedAt=" + openedAt);
    }
    if (closedAt != null) {
      lines.add("closedAt=" + closedAt);
    }

    return "[" + String.join(", ", lines) + "]";
  }
}

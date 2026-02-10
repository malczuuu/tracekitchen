package io.github.malczuuu.tracekitchen;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.Nullable;

final class SpanImpl implements Span {

  private final @Nullable String name;
  private final Trace trace;

  private final Clock clock;
  private final SpanLifecycleAdapter lifecycleAdapter;
  private final TraceFactory traceFactory;

  private final Map<String, String> attributes;

  private @Nullable Instant openedAt = null;
  private @Nullable Instant closedAt = null;

  SpanImpl(Clock clock, SpanLifecycleAdapter lifecycleAdapter, TraceFactory traceFactory) {
    this(
        null,
        new TraceImpl(traceFactory.makeTraceId(), traceFactory.makeSpanId()),
        clock,
        lifecycleAdapter,
        traceFactory);
  }

  SpanImpl(
      String name, Clock clock, SpanLifecycleAdapter lifecycleAdapter, TraceFactory traceFactory) {
    this(
        name,
        new TraceImpl(traceFactory.makeTraceId(), traceFactory.makeSpanId()),
        clock,
        lifecycleAdapter,
        traceFactory);
  }

  SpanImpl(
      @Nullable String name,
      Trace trace,
      Clock clock,
      SpanLifecycleAdapter lifecycleAdapter,
      TraceFactory traceFactory) {
    this(name, trace, null, clock, lifecycleAdapter, traceFactory);
  }

  private SpanImpl(
      @Nullable String name,
      Trace trace,
      @Nullable Map<String, String> attributes,
      Clock clock,
      SpanLifecycleAdapter lifecycleAdapter,
      TraceFactory traceFactory) {
    this.name = name;
    this.trace = trace;
    this.attributes = attributes != null ? new HashMap<>(attributes) : new HashMap<>();
    this.clock = clock;
    this.lifecycleAdapter = lifecycleAdapter;
    this.traceFactory = traceFactory;
  }

  @Override
  public Span spawnChild() {
    return new SpanImpl(
        name,
        trace.spawnChild(traceFactory.makeSpanId()),
        attributes,
        clock,
        lifecycleAdapter,
        traceFactory);
  }

  @Override
  public Span spawnChild(String name) {
    return new SpanImpl(
        name,
        trace.spawnChild(traceFactory.makeSpanId()),
        attributes,
        clock,
        lifecycleAdapter,
        traceFactory);
  }

  @Override
  public String getName() {
    return name != null ? name : "<anonymous>";
  }

  @Override
  public Trace getTrace() {
    return trace;
  }

  @Override
  public OpenSpan open() {
    Span previous = ThreadLocalSpanHolder.current();

    ThreadLocalSpanHolder.push(this);
    openAt(clock.instant());
    lifecycleAdapter.afterOpened(this, previous);

    return new OpenSpanWrapper(
        this,
        () -> {
          Span closed = ThreadLocalSpanHolder.pop();
          if (closed != this) {
            throw new IllegalStateException(
                "Span stack corrupted, expected to close " + this + " but was " + closed);
          }

          this.closeAt(clock.instant());
          Span current = ThreadLocalSpanHolder.current();
          lifecycleAdapter.afterClosed(this, current);
        });
  }

  void openAt(Instant time) {
    if (closedAt != null) {
      throw new IllegalStateException("Cannot open a closed span");
    }
    if (openedAt != null) {
      throw new IllegalStateException("Cannot open an already opened span");
    }
    openedAt = time;
  }

  void closeAt(Instant time) {
    if (openedAt == null) {
      throw new IllegalStateException("Cannot close a non-open span");
    }
    if (closedAt != null) {
      throw new IllegalStateException("Cannot close an already closed span");
    }
    if (time.isBefore(openedAt)) {
      throw new IllegalStateException("Cannot close span with time earlier than opening time");
    }
    closedAt = time;
  }

  @Override
  public SpanState getState() {
    if (closedAt != null) {
      return SpanState.CLOSED;
    }
    if (openedAt != null) {
      return SpanState.OPEN;
    }
    return SpanState.NEW;
  }

  @Override
  public Duration getDuration() {
    if (openedAt == null || closedAt == null) {
      return Duration.ZERO;
    }
    return Duration.between(openedAt, closedAt);
  }

  @Override
  public void setAttribute(String key, String value) {
    attributes.put(key, value);
  }

  @Override
  public @Nullable String getAttribute(String key) {
    return attributes.get(key);
  }

  @Override
  public String toString() {
    List<String> lines = new ArrayList<>();
    if (name != null) {
      lines.add("name='" + name + "'");
    }

    lines.add("traceId='" + trace.getTraceId() + "'");
    lines.add("spanId='" + trace.getSpanId() + "'");
    if (trace.getParentSpanId() != null) {
      lines.add("parentSpanId='" + trace.getParentSpanId() + "'");
    }
    lines.add("sampled=" + trace.isSampled());

    if (openedAt != null) {
      lines.add("openedAt=" + openedAt);
    }
    if (closedAt != null) {
      lines.add("closedAt=" + closedAt);
    }

    return "[" + String.join(", ", lines) + "]";
  }

  /** A convenience implementation of {@link OpenSpan}. */
  protected static class OpenSpanWrapper implements OpenSpan {

    private final Span span;
    private final Runnable close;
    private boolean open = true;

    /**
     * Wraps span with procedure what to do on closing.
     *
     * @param span the current span
     * @param close the action to execute on closing
     */
    protected OpenSpanWrapper(Span span, Runnable close) {
      this.span = span;
      this.close = close;
    }

    /**
     * {@inheritDoc}
     *
     * @return the non-null active {@link Span}
     */
    @Override
    public Span getSpan() {
      return span;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Guards from multiple closing attempts.
     */
    @Override
    public void close() {
      if (open) {
        close.run();
        open = false;
      }
    }
  }
}

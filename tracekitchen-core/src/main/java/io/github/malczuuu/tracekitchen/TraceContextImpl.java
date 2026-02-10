package io.github.malczuuu.tracekitchen;

import java.time.Clock;
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

  private final Clock clock;
  private final TraceContextLifecycleAdapter lifecycleAdapter;
  private final TraceFactory traceFactory;

  private @Nullable Instant openedAt = null;
  private @Nullable Instant closedAt = null;

  TraceContextImpl(
      Clock clock, TraceContextLifecycleAdapter lifecycleAdapter, TraceFactory traceFactory) {
    this(
        null,
        traceFactory.makeTraceId(),
        traceFactory.makeSpanId(),
        null,
        clock,
        lifecycleAdapter,
        traceFactory);
  }

  TraceContextImpl(
      String name,
      Clock clock,
      TraceContextLifecycleAdapter lifecycleAdapter,
      TraceFactory traceFactory) {
    this(
        name,
        traceFactory.makeTraceId(),
        traceFactory.makeSpanId(),
        null,
        clock,
        lifecycleAdapter,
        traceFactory);
  }

  TraceContextImpl(
      @Nullable String name,
      String traceId,
      String spanId,
      @Nullable String parentSpanId,
      Clock clock,
      TraceContextLifecycleAdapter lifecycleAdapter,
      TraceFactory traceFactory) {
    this.name = name;
    this.traceId = traceId;
    this.spanId = spanId;
    this.parentSpanId = parentSpanId;
    this.clock = clock;
    this.lifecycleAdapter = lifecycleAdapter;
    this.traceFactory = traceFactory;
  }

  @Override
  public TraceContext makeChild() {
    return new TraceContextImpl(
        name, traceId, traceFactory.makeSpanId(), spanId, clock, lifecycleAdapter, traceFactory);
  }

  @Override
  public TraceContext makeChild(String name) {
    return new TraceContextImpl(
        name, traceId, traceFactory.makeSpanId(), spanId, clock, lifecycleAdapter, traceFactory);
  }

  @Override
  public String getName() {
    return name != null ? name : "<anonymous>";
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
  public OpenContext open() {
    TraceContext previousContext = ContextThreadLocalHolder.current();

    ContextThreadLocalHolder.push(this);
    openAt(clock.instant());
    lifecycleAdapter.afterOpened(this, previousContext);

    return new OpenContextWrapper(
        this,
        () -> {
          TraceContext closed = ContextThreadLocalHolder.pop();
          if (closed != this) {
            throw new IllegalStateException(
                "Context stack corrupted, expected to close " + this + " but was " + closed);
          }

          this.closeAt(clock.instant());
          TraceContext currentContext = ContextThreadLocalHolder.current();
          lifecycleAdapter.afterClosed(this, currentContext);
        });
  }

  void openAt(Instant time) {
    if (closedAt != null) {
      throw new IllegalStateException("Cannot open a closed context");
    }
    if (openedAt != null) {
      throw new IllegalStateException("Cannot open an already opened context");
    }
    openedAt = time;
  }

  void closeAt(Instant time) {
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

  /** A convenience implementation of {@link OpenContext}. */
  protected static class OpenContextWrapper implements OpenContext {

    private final TraceContext context;
    private final Runnable close;
    private boolean open = true;

    /**
     * Wraps context with procedure what to do on closing.
     *
     * @param context the current context
     * @param close the action to execute on closing
     */
    protected OpenContextWrapper(TraceContext context, Runnable close) {
      this.context = context;
      this.close = close;
    }

    /**
     * {@inheritDoc}
     *
     * @return the non-null active {@link TraceContext}
     */
    @Override
    public TraceContextSnapshot getContext() {
      return context;
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

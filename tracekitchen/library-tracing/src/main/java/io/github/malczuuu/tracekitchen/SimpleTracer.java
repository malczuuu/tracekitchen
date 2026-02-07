package io.github.malczuuu.tracekitchen;

import java.time.Clock;
import org.jspecify.annotations.Nullable;

/**
 * Simple implementation of the {@link Tracer} interface.
 *
 * <p>This tracer provides a minimal, thread-safe tracing mechanism suitable for synchronous and
 * asynchronous applications. It supports creating trace contexts, building contexts from incoming
 * data, opening scopes, and automatically managing {@code MDC} values.
 *
 * <h2>Features:</h2>
 *
 * <ul>
 *   <li>Create root or child {@link TraceContext} instances
 *   <li>Build {@link TraceContext} from extracted or custom values
 *   <li>Open a {@link OpenContext} scope using {@code try-with-resources}
 *   <li>Automatically push/pop context to {@code ThreadLocal} stack
 *   <li>Automatically populate {@code MDC} with trace identifiers
 * </ul>
 */
public class SimpleTracer implements Tracer {

  private final TraceFactory traceFactory;
  private final LoggingContextAdapter loggingContextAdapter;
  private final ContextLifecycleAdapter contextLifecycleAdapter;
  private final Clock clock;

  SimpleTracer(
      TraceFactory traceFactory,
      LoggingContextAdapter loggingContextAdapter,
      ContextLifecycleAdapter contextLifecycleAdapter,
      Clock clock) {
    this.traceFactory = traceFactory;
    this.loggingContextAdapter = loggingContextAdapter;
    this.contextLifecycleAdapter = contextLifecycleAdapter;
    this.clock = clock;
  }

  /** {@inheritDoc} */
  @Override
  public TraceContext createContext() {
    return new TraceContextImpl(traceFactory);
  }

  /** {@inheritDoc} */
  @Override
  public TraceContextBuilder createBuilder() {
    return new TraceContextBuilderImpl(traceFactory);
  }

  /**
   * {@inheritDoc}
   *
   * <p>The given {@link TraceContext} becomes active in the current thread ({@code ThreadLocal})
   * and is also written into the {@code MDC} under keys {@code traceId}, {@code spanId}, and {@code
   * parentSpanId}. Closing the returned {@link OpenContext} will restore the previous context.
   */
  @Override
  public OpenContext open(TraceContext context) {
    ContextThreadLocalHolder.push(context);
    context.open(clock.instant());
    contextLifecycleAdapter.onContextOpened(context);
    synchronizeContext();

    return new OpenContextWrapper(
        context,
        () -> {
          TraceContext closed = ContextThreadLocalHolder.pop();
          if (closed != null) {
            closed.close(clock.instant());
            contextLifecycleAdapter.onContextClosed(closed);
          }
          synchronizeContext();
        });
  }

  /**
   * {@inheritDoc}
   *
   * @return the currently active {@link TraceContext}, or {@code null} if none
   */
  @Override
  public @Nullable TraceContext getCurrentContext() {
    return ContextThreadLocalHolder.current();
  }

  private void synchronizeContext() {
    TraceContext ctx = ContextThreadLocalHolder.current();
    if (ctx != null) {
      loggingContextAdapter.push(ctx);
    } else {
      loggingContextAdapter.drop();
    }
  }

  private static final class OpenContextWrapper implements OpenContext {

    private final TraceContext context;
    private final Runnable close;

    OpenContextWrapper(TraceContext context, Runnable close) {
      this.context = context;
      this.close = close;
    }

    @Override
    public TraceContext getContext() {
      return context;
    }

    @Override
    public void close() {
      close.run();
    }
  }
}

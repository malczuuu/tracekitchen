package io.github.malczuuu.tracekitchen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mercateo.test.clock.TestClock;
import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TraceContextImplTest {

  private Clock clock;
  private TraceContextLifecycleAdapter lifecycleAdapter;
  private TraceFactory traceFactory;

  @BeforeEach
  void beforeEach() {
    clock = TestClock.fixed(OffsetDateTime.parse("2025-09-22T12:33:17Z"));
    lifecycleAdapter = CompositeLifecycleAdapter.empty();
    traceFactory = SimpleTraceFactory.getInstance();
  }

  @Test
  void givenContext_whenSpawningChild_shouldRetainParentSpanId() {
    TraceContext parent = new TraceContextImpl(clock, lifecycleAdapter, traceFactory);

    TraceContext child = parent.makeChild();

    assertThat(parent.getParentSpanId()).isNull();
    assertThat(child.getTraceId()).isEqualTo(parent.getTraceId());
    assertThat(child.getSpanId()).isNotEqualTo(parent.getSpanId());
    assertThat(child.getParentSpanId()).isEqualTo(parent.getSpanId());
    assertThat(child.getState()).isEqualTo(ContextState.NEW);
  }

  @Test
  void givenOpenContext_whenOpening_shouldThrowException() {
    TraceContextImpl context = new TraceContextImpl(clock, lifecycleAdapter, traceFactory);

    context.openAt(Instant.parse("2025-03-19T09:53:11Z"));

    assertThat(context.getState()).isEqualTo(ContextState.OPEN);
    assertThatThrownBy(() -> context.openAt(Instant.parse("2025-03-19T09:53:13Z")))
        .isInstanceOf(IllegalStateException.class)
        .matches(e -> e.getMessage().equals("Cannot open an already opened context"));
  }

  @Test
  void givenClosedContext_whenOpening_shouldThrowException() {
    TraceContextImpl context = new TraceContextImpl(clock, lifecycleAdapter, traceFactory);

    context.openAt(Instant.parse("2025-03-19T09:53:11Z"));
    context.closeAt(Instant.parse("2025-03-19T09:53:12Z"));

    assertThat(context.getState()).isEqualTo(ContextState.CLOSED);
    assertThatThrownBy(() -> context.openAt(Instant.parse("2025-03-19T09:53:13Z")))
        .isInstanceOf(IllegalStateException.class)
        .matches(e -> e.getMessage().equals("Cannot open a closed context"));
  }

  @Test
  void givenNotOpenContext_whenClosing_shouldThrowException() {
    TraceContextImpl context = new TraceContextImpl(clock, lifecycleAdapter, traceFactory);

    assertThat(context.getState()).isEqualTo(ContextState.NEW);
    assertThatThrownBy(() -> context.closeAt(Instant.parse("2025-03-19T09:53:11Z")))
        .isInstanceOf(IllegalStateException.class)
        .matches(e -> e.getMessage().equals("Cannot close a non-open context"));
  }

  @Test
  void givenClosedContext_whenClosing_shouldThrowException() {
    TraceContextImpl context = new TraceContextImpl(clock, lifecycleAdapter, traceFactory);

    context.openAt(Instant.parse("2025-03-19T09:53:11Z"));
    context.closeAt(Instant.parse("2025-03-19T09:53:12Z"));

    assertThat(context.getState()).isEqualTo(ContextState.CLOSED);
    assertThatThrownBy(() -> context.closeAt(Instant.parse("2025-03-19T09:53:13Z")))
        .isInstanceOf(IllegalStateException.class)
        .matches(e -> e.getMessage().equals("Cannot close an already closed context"));
  }

  @Test
  void givenOpenContext_whenClosingWithTimeEarlierThanOpening_shouldThrowException() {
    TraceContextImpl context = new TraceContextImpl(clock, lifecycleAdapter, traceFactory);

    context.openAt(Instant.parse("2025-03-19T09:53:14Z"));

    assertThat(context.getState()).isEqualTo(ContextState.OPEN);
    assertThatThrownBy(() -> context.closeAt(Instant.parse("2025-03-19T09:53:13Z")))
        .isInstanceOf(IllegalStateException.class)
        .matches(
            e -> e.getMessage().equals("Cannot close context with time earlier than opening time"));
  }
}

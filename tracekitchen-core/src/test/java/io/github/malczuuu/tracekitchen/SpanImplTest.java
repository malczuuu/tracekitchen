package io.github.malczuuu.tracekitchen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mercateo.test.clock.TestClock;
import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SpanImplTest {

  private Clock clock;
  private SpanLifecycleAdapter lifecycleAdapter;
  private TraceFactory traceFactory;

  @BeforeEach
  void beforeEach() {
    clock = TestClock.fixed(OffsetDateTime.parse("2025-09-22T12:33:17Z"));
    lifecycleAdapter = CompositeLifecycleAdapter.empty();
    traceFactory = SimpleTraceFactory.getInstance();
  }

  @Test
  void givenContext_whenSpawningChild_shouldRetainParentSpanId() {
    Span parent = new SpanImpl(clock, lifecycleAdapter, traceFactory);

    Span child = parent.spawnChild();

    assertThat(parent.getTrace().getParentSpanId()).isNull();
    assertThat(child.getTrace()).matches(it -> it.isChildOf(parent.getTrace()));
    assertThat(child.getState()).isEqualTo(SpanState.NEW);
  }

  @Test
  void givenOpenContext_whenOpening_shouldThrowException() {
    SpanImpl context = new SpanImpl(clock, lifecycleAdapter, traceFactory);

    context.openAt(Instant.parse("2025-03-19T09:53:11Z"));

    assertThat(context.getState()).isEqualTo(SpanState.OPEN);
    assertThatThrownBy(() -> context.openAt(Instant.parse("2025-03-19T09:53:13Z")))
        .isInstanceOf(IllegalStateException.class)
        .matches(e -> e.getMessage().equals("Cannot open an already opened span"));
  }

  @Test
  void givenClosedContext_whenOpening_shouldThrowException() {
    SpanImpl context = new SpanImpl(clock, lifecycleAdapter, traceFactory);

    context.openAt(Instant.parse("2025-03-19T09:53:11Z"));
    context.closeAt(Instant.parse("2025-03-19T09:53:12Z"));

    assertThat(context.getState()).isEqualTo(SpanState.CLOSED);
    assertThatThrownBy(() -> context.openAt(Instant.parse("2025-03-19T09:53:13Z")))
        .isInstanceOf(IllegalStateException.class)
        .matches(e -> e.getMessage().equals("Cannot open a closed span"));
  }

  @Test
  void givenNotOpenContext_whenClosing_shouldThrowException() {
    SpanImpl context = new SpanImpl(clock, lifecycleAdapter, traceFactory);

    assertThat(context.getState()).isEqualTo(SpanState.NEW);
    assertThatThrownBy(() -> context.closeAt(Instant.parse("2025-03-19T09:53:11Z")))
        .isInstanceOf(IllegalStateException.class)
        .matches(e -> e.getMessage().equals("Cannot close a non-open span"));
  }

  @Test
  void givenClosedContext_whenClosing_shouldThrowException() {
    SpanImpl context = new SpanImpl(clock, lifecycleAdapter, traceFactory);

    context.openAt(Instant.parse("2025-03-19T09:53:11Z"));
    context.closeAt(Instant.parse("2025-03-19T09:53:12Z"));

    assertThat(context.getState()).isEqualTo(SpanState.CLOSED);
    assertThatThrownBy(() -> context.closeAt(Instant.parse("2025-03-19T09:53:13Z")))
        .isInstanceOf(IllegalStateException.class)
        .matches(e -> e.getMessage().equals("Cannot close an already closed span"));
  }

  @Test
  void givenOpenContext_whenClosingWithTimeEarlierThanOpening_shouldThrowException() {
    SpanImpl context = new SpanImpl(clock, lifecycleAdapter, traceFactory);

    context.openAt(Instant.parse("2025-03-19T09:53:14Z"));

    assertThat(context.getState()).isEqualTo(SpanState.OPEN);
    assertThatThrownBy(() -> context.closeAt(Instant.parse("2025-03-19T09:53:13Z")))
        .isInstanceOf(IllegalStateException.class)
        .matches(
            e -> e.getMessage().equals("Cannot close span with time earlier than opening time"));
  }
}

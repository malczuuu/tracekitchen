package io.github.malczuuu.tracekit.simple;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mercateo.test.clock.TestClock;
import io.github.malczuuu.tracekit.Span;
import io.github.malczuuu.tracekit.SpanLifecycleAdapter;
import io.github.malczuuu.tracekit.SpanState;
import io.github.malczuuu.tracekit.TraceFactory;
import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SimpleSpanTest {

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
    Span parent = new SimpleSpan(clock, lifecycleAdapter, traceFactory);

    Span child = parent.spawnChild();

    assertThat(parent.getTrace().getParentSpanId()).isNull();
    assertThat(child.getTrace()).matches(it -> it.isChildOf(parent.getTrace()));
    assertThat(child.getState()).isEqualTo(SpanState.NEW);
  }

  @Test
  void givenOpenContext_whenOpening_shouldThrowException() {
    SimpleSpan span = new SimpleSpan(clock, lifecycleAdapter, traceFactory);

    span.openAt(Instant.parse("2025-03-19T09:53:11Z"));

    assertThat(span.getState()).isEqualTo(SpanState.OPEN);
    assertThatThrownBy(() -> span.openAt(Instant.parse("2025-03-19T09:53:13Z")))
        .isInstanceOf(IllegalStateException.class)
        .matches(e -> e.getMessage().equals("Cannot open an already opened span"));
  }

  @Test
  void givenClosedContext_whenOpening_shouldThrowException() {
    SimpleSpan span = new SimpleSpan(clock, lifecycleAdapter, traceFactory);

    span.openAt(Instant.parse("2025-03-19T09:53:11Z"));
    span.closeAt(Instant.parse("2025-03-19T09:53:12Z"));

    assertThat(span.getState()).isEqualTo(SpanState.CLOSED);
    assertThatThrownBy(() -> span.openAt(Instant.parse("2025-03-19T09:53:13Z")))
        .isInstanceOf(IllegalStateException.class)
        .matches(e -> e.getMessage().equals("Cannot open a closed span"));
  }

  @Test
  void givenNotOpenContext_whenClosing_shouldThrowException() {
    SimpleSpan span = new SimpleSpan(clock, lifecycleAdapter, traceFactory);

    assertThat(span.getState()).isEqualTo(SpanState.NEW);
    assertThatThrownBy(() -> span.closeAt(Instant.parse("2025-03-19T09:53:11Z")))
        .isInstanceOf(IllegalStateException.class)
        .matches(e -> e.getMessage().equals("Cannot close a non-open span"));
  }

  @Test
  void givenClosedContext_whenClosing_shouldThrowException() {
    SimpleSpan span = new SimpleSpan(clock, lifecycleAdapter, traceFactory);

    span.openAt(Instant.parse("2025-03-19T09:53:11Z"));
    span.closeAt(Instant.parse("2025-03-19T09:53:12Z"));

    assertThat(span.getState()).isEqualTo(SpanState.CLOSED);
    assertThatThrownBy(() -> span.closeAt(Instant.parse("2025-03-19T09:53:13Z")))
        .isInstanceOf(IllegalStateException.class)
        .matches(e -> e.getMessage().equals("Cannot close an already closed span"));
  }

  @Test
  void givenOpenContext_whenClosingWithTimeEarlierThanOpening_shouldThrowException() {
    SimpleSpan span = new SimpleSpan(clock, lifecycleAdapter, traceFactory);

    span.openAt(Instant.parse("2025-03-19T09:53:14Z"));

    assertThat(span.getState()).isEqualTo(SpanState.OPEN);
    assertThatThrownBy(() -> span.closeAt(Instant.parse("2025-03-19T09:53:13Z")))
        .isInstanceOf(IllegalStateException.class)
        .matches(
            e -> e.getMessage().equals("Cannot close span with time earlier than opening time"));
  }
}

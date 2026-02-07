package io.github.malczuuu.tracekitchen.tracing.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TraceContextImplTest {

  private TraceFactory traceFactory;

  @BeforeEach
  void beforeEach() {
    traceFactory = SimpleTraceFactory.getInstance();
  }

  @Test
  void givenContext_whenSpawningChild_shouldRetainParentSpanId() {
    TraceContext parent = new TraceContextImpl(traceFactory);

    TraceContext child = parent.makeChild();

    assertThat(parent.getParentSpanId()).isNull();
    assertThat(child.getTraceId()).isEqualTo(parent.getTraceId());
    assertThat(child.getSpanId()).isNotEqualTo(parent.getSpanId());
    assertThat(child.getParentSpanId()).isEqualTo(parent.getSpanId());
    assertThat(child.getState()).isEqualTo(ContextState.NEW);
  }

  @Test
  void givenOpenContext_whenOpening_shouldThrowException() {
    TraceContext context = new TraceContextImpl(traceFactory);

    context.open(Instant.parse("2025-03-19T09:53:11Z"));

    assertThat(context.getState()).isEqualTo(ContextState.OPEN);
    assertThatThrownBy(() -> context.open(Instant.parse("2025-03-19T09:53:13Z")))
        .isInstanceOf(IllegalStateException.class)
        .matches(e -> e.getMessage().equals("Cannot open an already opened context"));
  }

  @Test
  void givenClosedContext_whenOpening_shouldThrowException() {
    TraceContext context = new TraceContextImpl(traceFactory);

    context.open(Instant.parse("2025-03-19T09:53:11Z"));
    context.close(Instant.parse("2025-03-19T09:53:12Z"));

    assertThat(context.getState()).isEqualTo(ContextState.CLOSED);
    assertThatThrownBy(() -> context.open(Instant.parse("2025-03-19T09:53:13Z")))
        .isInstanceOf(IllegalStateException.class)
        .matches(e -> e.getMessage().equals("Cannot open a closed context"));
  }

  @Test
  void givenNotOpenContext_whenClosing_shouldThrowException() {
    TraceContext context = new TraceContextImpl(traceFactory);

    assertThat(context.getState()).isEqualTo(ContextState.NEW);
    assertThatThrownBy(() -> context.close(Instant.parse("2025-03-19T09:53:11Z")))
        .isInstanceOf(IllegalStateException.class)
        .matches(e -> e.getMessage().equals("Cannot close a non-open context"));
  }

  @Test
  void givenClosedContext_whenClosing_shouldThrowException() {
    TraceContext context = new TraceContextImpl(traceFactory);

    context.open(Instant.parse("2025-03-19T09:53:11Z"));
    context.close(Instant.parse("2025-03-19T09:53:12Z"));

    assertThat(context.getState()).isEqualTo(ContextState.CLOSED);
    assertThatThrownBy(() -> context.close(Instant.parse("2025-03-19T09:53:13Z")))
        .isInstanceOf(IllegalStateException.class)
        .matches(e -> e.getMessage().equals("Cannot close an already closed context"));
  }

  @Test
  void givenOpenContext_whenClosingWithTimeEarlierThanOpening_shouldThrowException() {
    TraceContext context = new TraceContextImpl(traceFactory);

    context.open(Instant.parse("2025-03-19T09:53:14Z"));

    assertThat(context.getState()).isEqualTo(ContextState.OPEN);
    assertThatThrownBy(() -> context.close(Instant.parse("2025-03-19T09:53:13Z")))
        .isInstanceOf(IllegalStateException.class)
        .matches(
            e -> e.getMessage().equals("Cannot close context with time earlier than opening time"));
  }
}

package io.github.malczuuu.tracekit.simple;

import static org.assertj.core.api.Assertions.assertThat;

import com.mercateo.test.clock.TestClock;
import io.github.malczuuu.tracekit.Span;
import io.github.malczuuu.tracekit.SpanLifecycleAdapter;
import io.github.malczuuu.tracekit.TraceFactory;
import java.time.Clock;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ThreadLocalSpanHolderTest {

  private Clock clock;
  private SpanLifecycleAdapter lifecycleAdapter;
  private TraceFactory traceFactory;

  @BeforeEach
  void beforeEach() {
    clock = TestClock.fixed(OffsetDateTime.parse("2025-09-22T12:33:17Z"));
    lifecycleAdapter = CompositeLifecycleAdapter.empty();
    traceFactory = SimpleTraceFactory.getInstance();
  }

  @AfterEach
  void afterEach() {
    while (ThreadLocalSpanHolder.current() != null) {
      ThreadLocalSpanHolder.pop();
    }
  }

  @Test
  void shouldReturnNullWhenNoContextIsPresent() {
    assertThat(ThreadLocalSpanHolder.current()).isNull();
  }

  @Test
  void shouldReturnCurrentContextAfterPush() {
    Span span = fakeContext("trace-1", "span-1");

    ThreadLocalSpanHolder.push(span);

    assertThat(ThreadLocalSpanHolder.current()).isSameAs(span);
  }

  @Test
  void shouldRestorePreviousContextAfterPop() {
    Span span1 = fakeContext("trace-1", "span-1");
    Span span2 = fakeContext("trace-2", "span-2");

    ThreadLocalSpanHolder.push(span1);
    ThreadLocalSpanHolder.push(span2);

    assertThat(ThreadLocalSpanHolder.current()).isSameAs(span2);

    ThreadLocalSpanHolder.pop();

    assertThat(ThreadLocalSpanHolder.current()).isSameAs(span1);
  }

  @Test
  void shouldReturnNullAfterPoppingLastContext() {
    Span span = fakeContext("trace-1", "span-1");

    ThreadLocalSpanHolder.push(span);
    ThreadLocalSpanHolder.pop();

    assertThat(ThreadLocalSpanHolder.current()).isNull();
  }

  @Test
  void popOnEmptyStackShouldDoNothing() {
    ThreadLocalSpanHolder.pop();

    assertThat(ThreadLocalSpanHolder.current()).isNull();
  }

  private Span fakeContext(String traceId, String spanId) {
    return new SimpleSpan(
        null, new SimpleTrace(traceId, spanId), clock, lifecycleAdapter, traceFactory);
  }
}

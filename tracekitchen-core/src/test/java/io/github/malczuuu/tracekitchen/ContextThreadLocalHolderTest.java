package io.github.malczuuu.tracekitchen;

import static org.assertj.core.api.Assertions.assertThat;

import com.mercateo.test.clock.TestClock;
import java.time.Clock;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContextThreadLocalHolderTest {

  private Clock clock;
  private TraceContextLifecycleAdapter lifecycleAdapter;
  private TraceFactory traceFactory;

  @BeforeEach
  void beforeEach() {
    clock = TestClock.fixed(OffsetDateTime.parse("2025-09-22T12:33:17Z"));
    lifecycleAdapter = CompositeLifecycleAdapter.empty();
    traceFactory = SimpleTraceFactory.getInstance();
  }

  @AfterEach
  void afterEach() {
    while (ContextThreadLocalHolder.current() != null) {
      ContextThreadLocalHolder.pop();
    }
  }

  @Test
  void shouldReturnNullWhenNoContextIsPresent() {
    assertThat(ContextThreadLocalHolder.current()).isNull();
  }

  @Test
  void shouldReturnCurrentContextAfterPush() {
    TraceContext context = fakeContext("trace-1", "span-1");

    ContextThreadLocalHolder.push(context);

    assertThat(ContextThreadLocalHolder.current()).isSameAs(context);
  }

  @Test
  void shouldRestorePreviousContextAfterPop() {
    TraceContext context1 = fakeContext("trace-1", "span-1");
    TraceContext context2 = fakeContext("trace-2", "span-2");

    ContextThreadLocalHolder.push(context1);
    ContextThreadLocalHolder.push(context2);

    assertThat(ContextThreadLocalHolder.current()).isSameAs(context2);

    ContextThreadLocalHolder.pop();

    assertThat(ContextThreadLocalHolder.current()).isSameAs(context1);
  }

  @Test
  void shouldReturnNullAfterPoppingLastContext() {
    TraceContext context = fakeContext("trace-1", "span-1");

    ContextThreadLocalHolder.push(context);
    ContextThreadLocalHolder.pop();

    assertThat(ContextThreadLocalHolder.current()).isNull();
  }

  @Test
  void popOnEmptyStackShouldDoNothing() {
    ContextThreadLocalHolder.pop();

    assertThat(ContextThreadLocalHolder.current()).isNull();
  }

  private TraceContext fakeContext(String traceId, String spanId) {
    return new TraceContextImpl(null, traceId, spanId, null, clock, lifecycleAdapter, traceFactory);
  }
}

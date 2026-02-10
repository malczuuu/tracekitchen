package io.github.malczuuu.tracekitchen;

import static org.assertj.core.api.Assertions.assertThat;

import com.mercateo.test.clock.TestClock;
import java.time.Clock;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SpanBuilderImplTest {

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
  void givenBuilder_whenFillingValues_shouldConstructNewObjectOnEachStep() {
    SpanBuilder builder = new SpanBuilderImpl(clock, lifecycleAdapter, traceFactory);

    var afterStep1 = builder.withTraceId("traceId");
    var afterStep2 = afterStep1.withSpanId("spanId");
    var afterStep3 = afterStep2.withParentSpanId("parentSpanId");

    Span span = afterStep3.build();

    assertThat(builder).isNotSameAs(afterStep1);
    assertThat(afterStep1).isNotSameAs(afterStep2);
    assertThat(afterStep2).isNotSameAs(afterStep3);

    assertThat(span.getTrace().getTraceId()).isEqualTo("traceId");
    assertThat(span.getTrace().getSpanId()).isEqualTo("spanId");
    assertThat(span.getTrace().getParentSpanId()).isEqualTo("parentSpanId");
  }
}

package io.github.malczuuu.tracekitchen;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TraceContextBuilderImplTest {

  @Test
  void givenBuilder_whenFillingValues_shouldConstructNewObjectOnEachStep() {
    TraceContextBuilder builder = new TraceContextBuilderImpl(SimpleTraceFactory.getInstance());

    var afterStep1 = builder.withTraceId("traceId");
    var afterStep2 = afterStep1.withSpanId("spanId");
    var afterStep3 = afterStep2.withParentSpanId("parentSpanId");

    TraceContext context = afterStep3.build();

    assertThat(builder).isNotSameAs(afterStep1);
    assertThat(afterStep1).isNotSameAs(afterStep2);
    assertThat(afterStep2).isNotSameAs(afterStep3);

    assertThat(context.getTraceId()).isEqualTo("traceId");
    assertThat(context.getSpanId()).isEqualTo("spanId");
    assertThat(context.getParentSpanId()).isEqualTo("parentSpanId");
  }
}

package io.github.malczuuu.tracekitchen;

import java.time.Duration;
import org.jspecify.annotations.Nullable;

/** Represents an openable span, to be opened within {@code try-with-resources} blocks. */
public interface Span {

  /**
   * Creates a child of this span.
   *
   * <p>The child span inherits the trace ID of this span, and sets span ID as its parent span ID.
   * The child has a new, unique span ID.
   *
   * @return a new child {@link Span}
   */
  Span spawnChild();

  /**
   * Creates a named child of this span.
   *
   * <p>The child span inherits the trace ID of this span, and sets span ID as its parent span ID.
   * The child has a new, unique span ID.
   *
   * @param name the name of the child; must not be {@code null} or blank
   * @return a new child {@link Span} with the given name
   */
  Span spawnChild(String name);

  /**
   * Returns the name of this span.
   *
   * @return non-null span name ({@code "<anonymous>"} if no name assigned)
   */
  String getName();

  /**
   * Returns the trace information associated with this span, including trace ID, span ID, and
   * parent span ID.
   *
   * @return the {@link Trace} associated with this span
   */
  Trace getTrace();

  /**
   * Returns the current state of the span. State depends on previously called (or not-called)
   * {@link Span#open()} and/or {@link OpenSpan#close()} methods.
   *
   * @return the {@link SpanState} representing the current state
   */
  SpanState getState();

  /**
   * Returns the total duration for which the span has been open.
   *
   * <p>The duration is zero if the span has never been opened or has not yet been closed.
   *
   * @return a {@link Duration} representing the elapsed time between the last {@link Span#open()}
   *     and/or {@link OpenSpan#close()} calls, or {@link Duration#ZERO} if the span is not fully
   *     opened and closed
   */
  Duration getDuration();

  /**
   * Sets a key-value attribute on this span. Attributes are useful for adding metadata to spans,
   * such as
   *
   * @param key the attribute key; must not be {@code null} or blank
   * @param value the attribute value; must not be {@code null}
   */
  void setAttribute(String key, String value);

  /**
   * Retrieves the value of an attribute by key.
   *
   * @param key the attribute key; must not be {@code null} or blank
   * @return the attribute value associated with the given key, or {@code null} if no such attribute
   *     exists
   */
  @Nullable String getAttribute(String key);

  /**
   * Opens the given span for the current execution scope.
   *
   * <p>Returns an {@link OpenSpan} that should be used in a {@code try-with-resources} block to
   * ensure the previous span is restored on close.
   *
   * @return an {@link OpenSpan} representing the active scope
   */
  OpenSpan open();
}

# TraceKitchen

[![Build Status](https://github.com/malczuuu/tracekitchen/actions/workflows/gradle-build.yml/badge.svg)](https://github.com/malczuuu/tracekitchen/actions/workflows/gradle-build.yml)
[![Sonatype](https://img.shields.io/maven-central/v/io.github.malczuuu.tracekit/tracekit)][maven-central]
[![License](https://img.shields.io/github/license/malczuuu/tracekitchen)](https://github.com/malczuuu/tracekitchen/blob/main/LICENSE)

What's cooking in here? A playground for exploring simple tracing model and traces propagation in **Spring Boot**.

## Table of Contents

- [Project Overview](#project-overview)
- [Core Modules](#core-modules)
  - [`tracekit`](#tracekit)
- [Spring Integration Modules](#spring-integration-modules)
  - [`tracekit-boot4:tracekit-boot4-core`](#tracekit-boot4tracekit-boot4-core)
  - [`tracekit-boot4:tracekit-boot4-aspect`](#tracekit-boot4tracekit-boot4-aspect)
  - [`tracekit-boot4:tracekit-boot4-restclient`](#tracekit-boot4tracekit-boot4-restclient)
  - [`tracekit-boot4:tracekit-boot4-webmvc`](#tracekit-boot4tracekit-boot4-webmvc)
- [Inspiration](#inspiration)
- [Compatibility with Existing Tracing Frameworks](#compatibility-with-existing-tracing-frameworks)
  - [Using with W3C Trace Context](#using-with-w3c-trace-context)
- [Internal Modules](#internal-modules)

## Project Overview

This repository is organized into **core library**, **Spring integrations**, and **internal applications** used for
validation and testing.

The goal of this structure is to keep the tracing model framework-agnostic while providing first-class integrations for
Spring-based applications.

## Core Modules

### `tracekit`

The foundational module of the project.

This module defines the **trace context model** and its lifecycle without any dependency on Spring or other frameworks.

Responsibilities include:

- `Tracer` abstraction for creating spans,
- root and child span creation,
- span hierarchy management,
- lifecycle handling (`open` / `close`) via `try-with-resources`,
- trace context propagation utilities for thread pools.

This module can be used in plain Java applications and serves as the base for all other integrations.

TraceKit's core concept is the **span** – a unit of work that can be traced. Each span has a unique identifier and is
part of a trace (which groups related spans).

**Span Hierarchy:**

- A **root span** starts a new trace with a new trace ID.
- **Child spans** inherit the trace ID from their parent and maintain a parent-span-id link.
- This creates a tree structure that can be correlated across logs and systems.

**Explicit Lifecycle:**

Spans are opened within `try-with-resources` blocks, making the start and end of operations visible:

```java
Tracer tracer = /* ... */;

// Create a root span
try (OpenSpan span = tracer.root("request").open()) {
  // Your operation here - the span is active
  doWork();
  
  // Create a child span
  try (OpenSpan childSpan = span.getSpan().spawnChild("database-query").open()) {
    // Database operation
    queryDatabase();
  }
  // Child span automatically closes and restores parent
}
// Root span closes and restores previous context
```

**Building from Headers:**

When receiving trace information from external sources (HTTP headers, messages), use the builder:

```java
Span span = tracer.builder()
  .withTraceId(headersMap.get("X-Trace-Id"))
  .withSpanId(headersMap.get("X-Span-Id"))
  .withParentSpanId(headersMap.get("X-Parent-Span-Id"))
  .withName("incoming-request")
  .build();

try (OpenSpan openSpan = span.open()) {
  // Process the request with propagated context
}
```

**Thread Pool Integration:**

For operations in thread pools or async contexts, use executor wrapping to propagate context:

```java
ExecutorService executor = tracer.wrap(Executors.newFixedThreadPool(10));

// Tasks executed via wrapped executor inherit the current trace context
executor.submit(() -> {
  // This runs with the trace context from when submit() was called
  doWork();
});
```

## Spring Integration Modules

These modules provide seamless integration of the core tracing model with Spring Boot 4 applications. They build on top
of the core `tracekit` module and handle framework-specific concerns like dependency injection, servlet filtering, HTTP
client interception, and aspect-oriented programming.

### `tracekit-boot4:tracekit-boot4-core`

Provides core Spring Boot 4 support for TraceKit.

This module adapts the core tracing model to Spring's runtime model and dependency injection system.

Responsibilities include:

- wiring core components as Spring beans,
- integration with application lifecycle,
- shared Spring abstractions used by other Spring modules.

This module does not define transport-specific behavior.

### `tracekit-boot4:tracekit-boot4-aspect`

Aspect-oriented integration for tracing in Spring Boot 4.

This module provides support for `@Traceable` annotation via AOP for automatic context opening/closing.

### `tracekit-boot4:tracekit-boot4-restclient`

Integration with Spring's HTTP client infrastructure in Spring Boot 4.

This module adds propagation interceptor for Spring Boot's `RestClient` and `RestTemplate`. Includes active trace
context into outgoing requests.

### `tracekit-boot4:tracekit-boot4-webmvc`

Server-side integration for Spring Boot 4 WebMVC.

This module adds extractor filer for Spring Boot's WebMVC stack. Consumes tracing headers from incoming HTTP requests to
propagate into application.

## Inspiration

This project was primarily inspired by [Micrometer Tracing](https://micrometer.io/docs/tracing) (formerly Spring Cloud
Sleuth). While Micrometer Tracing provides a comprehensive, production-grade observability facade, it comes with a
significant surface area - including tight coupling to the Observation API, baggage propagation, and multiple backend
bridges (Brave, OpenTelemetry).

TraceKit was born from the following conclusions:

- **Most applications need only basic trace propagation** - a trace ID, span ID, and parent span ID are sufficient for
  correlating logs across services.
- **A simpler API reduces cognitive overhead** - Micrometer's layered abstractions (`Observation` -> `Tracer` -> `Span`)
  are powerful but complex for straightforward use cases.
- **Lifecycle should be explicit** - spans are opened within `try-with-resources` blocks, making the start and end of
  traced operations visible in the code.

TraceKit intentionally does **not** aim to replace Micrometer Tracing. Instead, it offers a lightweight alternative for
projects where full observability infrastructure is unnecessary.

## Compatibility with Existing Tracing Frameworks

TraceKit uses **configurable HTTP headers** for trace context propagation. By default, no header names are set - you
configure them via application properties to match your environment:

```yaml
tracekit:
  trace-id-header-names: X-Trace-Id
  span-id-header-names: X-Span-Id
  parent-span-id-header-names: X-Parent-Span-Id
```

### Using with W3C Trace Context

TraceKit does **not** natively parse the [W3C `traceparent`](https://www.w3.org/TR/trace-context/) header format
(`00-<trace-id>-<span-id>-<flags>`). However, if your infrastructure propagates trace and span IDs as **separate
headers** (e.g. via a gateway or sidecar that decomposes `traceparent`), you can point TraceKit at those headers
directly.

For systems using [B3 propagation](https://github.com/openzipkin/b3-propagation) (Zipkin-style), configure:

```yaml
tracekit:
  trace-id-header-names: X-B3-TraceId
  span-id-header-names: X-B3-SpanId
  parent-span-id-header-names: X-B3-ParentSpanId
```

Multiple header names are supported (comma-separated), allowing TraceKit to coexist with other tracing systems by
reading from whichever header is present.

## Internal Modules

### `internal:library-bom`

A Bill of Materials (BOM) module.

It centralizes dependency management for internal test applications.

### `internal:library-common`

Shared internal utilities used by test applications.

### `internal:app-entrypoint`

An internal test application acting as an **entry-point service**.

Used to:

- validate incoming request tracing,
- test context creation at application boundaries,
- verify logging correlation and lifecycle behavior.

### `internal:app-downstream`

An internal test application acting as a **downstream service**.

Used to:

- validate trace context propagation across service boundaries,
- test client-side integrations,
- simulate multi-service tracing scenarios.

[maven-central]: https://central.sonatype.com/namespace/io.github.malczuuu.tracekit

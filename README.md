# TraceKitchen

What's cooking in here? A playground for exploring simple tracing model and traces propagation in **Spring Boot**.

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

- `TraceContext` and `Tracer` abstraction,
- root and child context creation,
- span hierarchy management,
- lifecycle handling (`open` / `close`),
- lifecycle callback adapters.

This module can be used in plain Java applications and serves as the base for all other integrations.

## Spring Integration Modules

### `tracekit-spring:tracekit-spring-core`

Provides core Spring support for TraceKitchen.

This module adapts the core tracing model to Spring's runtime model and dependency injection system.

Responsibilities include:

- wiring core components as Spring beans,
- integration with application lifecycle,
- shared Spring abstractions used by other Spring modules.

This module does not define transport-specific behavior.

### `tracekit-spring:tracekit-spring-aspect`

Aspect-oriented integration for tracing.

This module provides support for `@Traceable` annotation via AOP for automatic context opening/closing.

### `tracekit-spring:tracekit-spring-restclient`

Integration with Spring's HTTP client infrastructure.

This module adds propagation interceptor for Spring Boot's `RestClient` and `RestTemplate`. Includes active trace
context into outgoing requests.

### `tracekit-spring:tracekit-spring-webmvc`

Server-side integration for Spring WebMVC.

This module adds extractor filer for Spring Boot's WebMVC stack. Consumes tracing headers from incoming HTTP requests to
propagate into application.

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

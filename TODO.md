# TODO List

Must have:

- [ ] Integration with Actuator for metrics (decide what is there to metric at all).
- [ ] Documentation of how existing traceability frameworks inspired this project and what conclusions were established
  from experiments.
- [ ] Documentation of how this can be used with existing tracing frameworks (W3C format, etc.).
- [ ] Demo on how to integrate it with non-Spring HTTP clients (ex. Feign).

Nice to have (because main focus is on HTTP and blocking environment):

- [ ] Context propagation across Reactor and Spring WebFlux.
- [ ] Context propagation across reactive `WebClient` (most likely will be covered via previous task).
- [ ] Context propagation across `@KafkaListener`, `@RabbitListner` and a demo on how to integrate with other messaging
  systems (NATS for example).
- [ ] Context propagation across thread pools (ex. `ExecutorService`, `CompletableFuture` / `Future`, parallel streams).

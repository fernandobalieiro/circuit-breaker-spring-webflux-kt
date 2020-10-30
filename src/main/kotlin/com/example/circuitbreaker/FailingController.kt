package com.example.circuitbreaker

import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder.Resilience4JCircuitBreakerConfiguration
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.Optional

@RestController
class FailingController(
    private val failingService: FailingService,
    reactiveCircuitBreakerFactory: ReactiveCircuitBreakerFactory<Resilience4JCircuitBreakerConfiguration, Resilience4JConfigBuilder>
) {
    private val reactiveCircuitBreaker: ReactiveCircuitBreaker = reactiveCircuitBreakerFactory.create("greet")

    @GetMapping("/greet")
    fun greet(@RequestParam name: Optional<String>): Mono<String> {
        val results = failingService.greet(name)
        return reactiveCircuitBreaker.run(results) {
            Mono.just("Hello world!")
        }
    }
}

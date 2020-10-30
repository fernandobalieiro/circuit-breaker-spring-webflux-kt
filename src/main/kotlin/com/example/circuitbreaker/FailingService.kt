package com.example.circuitbreaker

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.Duration
import java.util.Optional

@Service
class FailingService {
    fun greet(name: Optional<String>): Mono<String> {
        val seconds = Math.random().times(10).toLong()
        return name.map { str ->
            Mono.just("Hello $str! (in $seconds seconds)")
        }
            .orElse(Mono.error(NullPointerException()))
            .delayElement(Duration.ofSeconds(seconds))
    }
}

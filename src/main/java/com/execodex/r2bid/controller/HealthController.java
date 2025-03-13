package com.execodex.r2bid.controller;

import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class HealthController {
    public Mono<String> health() {
        return Mono.just("OK");
    }
}

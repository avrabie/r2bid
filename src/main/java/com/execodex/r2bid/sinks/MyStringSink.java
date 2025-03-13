package com.execodex.r2bid.sinks;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public class MyStringSink {
    private final Sinks.Many<String> sink;


    public MyStringSink() {
        this.sink = Sinks.many().multicast().onBackpressureBuffer(3);
    }

    public void next(String value) {

        sink.tryEmitNext(value).orThrow();
    }

    public Flux<String> getFlux() {
        return sink.asFlux().share().onBackpressureBuffer(3);
    }

}

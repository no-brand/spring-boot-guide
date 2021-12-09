package com.nobrand.springbootcircuitbreaker.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
public class BookService {

    private final Logger log = LoggerFactory.getLogger(BookService.class);

    private final WebClient webClient;
    private final ReactiveCircuitBreaker readingListCircuitBreaker;

    public BookService(ReactiveCircuitBreakerFactory circuitBreakerFactory) {
        this.webClient = WebClient.builder().baseUrl("http://localhost:8080").build();
        this.readingListCircuitBreaker = circuitBreakerFactory.create("recommended");
    }

    public Mono<String> readingList() {
        return readingListCircuitBreaker.run(
                webClient
                        .get()
                        .uri("/recommended")
                        .retrieve()
                        .bodyToMono(String.class),
                throwable -> {
                    log.warn("error making request to book service", throwable);
                    return Mono.just("Cloud Native Java (O'Reilly)");
                }
        );
    }


}

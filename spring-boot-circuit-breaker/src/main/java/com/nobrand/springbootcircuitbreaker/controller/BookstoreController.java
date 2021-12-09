package com.nobrand.springbootcircuitbreaker.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@RestController
public class BookstoreController {

    @RequestMapping("/recommended")
    public Mono<String> readingList() {
        return Mono.just("Spring in Action (Manning), Cloud Native Java (O'Reilly), Learning Spring Boot (Packt)");
    }

    @RequestMapping("/to-read")
    public Mono<String> toRead() {
        return WebClient.builder().build()
                .get()
                .uri("localhost:8080/recommended")
                .retrieve()
                .bodyToMono(String.class);
    }

}

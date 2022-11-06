package com.receiver.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Slf4j
@RestController
public class ReceiverController {

    public static final String PROCESSOR_ENDPOINT = "localhost:8084/processor";

    @GetMapping("receiver")
    public Mono<List<Event>> receiveSomeString() {
        log.info("received some strings");
        return WebClient.create().get()
                .uri(uriBuilder -> uriBuilder.path(PROCESSOR_ENDPOINT)
                        .build())
                .retrieve()
                .bodyToFlux(Event.class)
                .collectList();
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Event {
    private int number;
    private String info;
}

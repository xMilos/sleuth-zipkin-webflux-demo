package com.receiver.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
public class ReceiverController {

    private final WebClient.Builder webClientBuilder;
    public static final String PROCESSOR_ENDPOINT = "http://localhost:8084/processor";

    @Autowired
    public ReceiverController(Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @GetMapping("receiver")
    public Mono<List<Event>> receiveSomeString() {
        log.info("received some strings");
        return webClientBuilder.baseUrl(PROCESSOR_ENDPOINT).build().get()
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

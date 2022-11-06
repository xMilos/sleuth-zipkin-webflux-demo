package com.processor.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Slf4j
@RestController
public class ProcessorController {

    public static final String EMITTER_ENDPOINT = "localhost:8083/emitter";

    @GetMapping("processor")
    public Flux<Event> processEvents() {
        log.info("processing some strings");
        return WebClient.create().get()
                .uri(uriBuilder -> uriBuilder.path(EMITTER_ENDPOINT)
                        .build())
                .retrieve()
                .bodyToFlux(Event.class)
                .map(v -> {
                    log.info("In flatMap operator, processing {} ", v);
                    if(v.getNumber() % 2 == 0){
                        v.setInfo("Even: " + v);
                    } else {
                        v.setInfo("Odd: " + v);
                    }
                    return v;
                }).delayElements(Duration.ofMillis(100));
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Event {
    private int number;
    private String info;
}
package com.processor.controller;

import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
public class ProcessorController {

    public static final String EMITTER_ENDPOINT = "http://localhost:8083/emitter";
    private final WebClient.Builder webClientBuilder;

    public ProcessorController(Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @GetMapping("processor")
    public Flux<Event> processEvents() {
        log.info("processing some strings");
        log.info("traceId should be: {}", MDC.get("traceId"));
        log.info("spanId should be: {}", MDC.get("spanId"));
        return webClientBuilder.baseUrl(EMITTER_ENDPOINT).build().get()
                .retrieve()
                .bodyToFlux(Event.class)
                .map(v -> {
                    log.info("In map operator, processing {} ", v);
                    if(v.getNumber() % 2 == 0){
                        v.setInfo("Even: " + v.getInfo());
                    } else {
                        v.setInfo("Odd: " + v.getInfo());
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
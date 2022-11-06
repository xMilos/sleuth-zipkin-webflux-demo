package com.emitter.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

import java.util.Arrays;
import java.util.List;

@RestController
@Slf4j
public class EmitterController {

    @GetMapping("emitter")
    public ParallelFlux<Event> emitEvents() {
        log.info("Getting events");
        return Flux.fromIterable(getListOfEvents())
                .parallel()
                .runOn(Schedulers.parallel())
                .map(v -> {
                    log.info("Event {}", v);
                    return v;
                });
    }

    private List<Event> getListOfEvents() {
        return Arrays.asList(
                new Event(0, "Zero"),
                new Event(1, "First"),
                new Event(2, "Second"),
                new Event(3, "Third"),
                new Event(4, "Fourth"),
                new Event(5, "Fifth"),
                new Event(6, "Sixth"),
                new Event(7, "Seventh"),
                new Event(8, "Eighth"),
                new Event(9, "Ninth")
        );
    }
}

@Data
@AllArgsConstructor
class Event {
    private int number;
    private String info;
}

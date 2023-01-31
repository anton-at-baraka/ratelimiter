package com.example.demo;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Executors;

@Service
public class TodoService {
    private final TodoApi api;

    public TodoService(TodoApi api) {
        this.api = api;
    }

    public void doStuff() {
        Flux.range(1, 10)
                .publishOn(Schedulers.fromExecutor(Executors.newFixedThreadPool(10)))
                .flatMap(i -> api.getResponse())
                .log()
                .blockLast();
    }
}

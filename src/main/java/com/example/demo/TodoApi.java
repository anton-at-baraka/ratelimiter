package com.example.demo;

import reactor.core.publisher.Mono;
import retrofit2.http.GET;

public interface TodoApi {

    @GET("/todos/1")
    Mono<TodoGetResponse> getResponse();
}

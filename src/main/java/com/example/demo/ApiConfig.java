package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakewharton.retrofit2.adapter.reactor.ReactorCallAdapterFactory;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Response;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
public class ApiConfig {

    @Bean
    public TodoApi someServiceApi(ObjectMapper objectMapper) {
        var rateLimiter = RateLimiter.of("TodoApiLimiter", () -> RateLimiterConfig.custom()
                .limitForPeriod(5)
                .limitRefreshPeriod(Duration.of(1, ChronoUnit.MINUTES))
                .timeoutDuration(Duration.of(1, ChronoUnit.MINUTES))
                .build());
        var client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    var request = chain.request();
                    if (rateLimiter.acquirePermission()) {
                        return chain.proceed(request);
                    } else {
                        chain.call().cancel();
                        return new Response.Builder()
                                .protocol(Protocol.HTTP_1_1)
                                .code(HttpStatus.TOO_MANY_REQUESTS.value())
                                .message(HttpStatus.TOO_MANY_REQUESTS.name())
                                .request(request)
                                .build();
                    }
                })
                .build();
        var retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .addCallAdapterFactory(ReactorCallAdapterFactory.create())
                .client(client)
                .build();

        return retrofit.create(TodoApi.class);
    }
}

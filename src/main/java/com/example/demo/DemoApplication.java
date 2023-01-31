package com.example.demo;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DemoApplication implements ApplicationRunner {

    private final TodoService service;
    private final ConfigurableApplicationContext context;

    public DemoApplication(TodoService service, ConfigurableApplicationContext context) {
        this.service = service;
        this.context = context;
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(DemoApplication.class).web(WebApplicationType.NONE).run(args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        service.doStuff();
        System.exit(0);
    }
}

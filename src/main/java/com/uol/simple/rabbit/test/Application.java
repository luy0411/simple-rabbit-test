package com.uol.simple.rabbit.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Import(RabbitConfiguration.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}

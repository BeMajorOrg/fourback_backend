package com.fourback.bemajor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableJpaAuditing
@SpringBootApplication
@EnableScheduling
public class BemajorApplication {
    public static void main(String[] args) {
        SpringApplication.run(BemajorApplication.class, args);
    }

}

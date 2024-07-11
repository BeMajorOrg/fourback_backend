package com.fourback.bemajor;

import com.fourback.bemajor.domain.Board;
import com.fourback.bemajor.repository.BoardRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing
@SpringBootApplication
public class BemajorApplication {
    public static void main(String[] args) {
        SpringApplication.run(BemajorApplication.class, args);




    }

}

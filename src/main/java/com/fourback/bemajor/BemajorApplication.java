package com.fourback.bemajor;

import com.fourback.bemajor.domain.Post;
import com.fourback.bemajor.repository.*;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BemajorApplication {

    public static void main(String[] args) {
        SpringApplication.run(BemajorApplication.class, args);
    }

    @Bean
    public ApplicationRunner runner(PostRepository postRepo,
                                    CommentRepository commentRepo)
            throws Exception {
        return (args) -> {
            if (args.containsOption("insertTestData")) {

                for(int i = 0; i < 20; i++) {
                    postRepo.save(Post.builder().title("제목" + i).content("내용" + i).build());
                }
            }
        };
    }
    }

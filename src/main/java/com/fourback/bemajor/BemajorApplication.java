package com.fourback.bemajor;

import com.fourback.bemajor.domain.friend.entity.Friend;
import com.fourback.bemajor.domain.friend.entity.FriendId;
import com.fourback.bemajor.domain.friend.repository.FriendRepository;
import com.fourback.bemajor.domain.user.entity.UserEntity;
import com.fourback.bemajor.domain.user.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.userdetails.User;


@EnableJpaAuditing
@SpringBootApplication
@EnableScheduling
public class BemajorApplication {
    public static void main(String[] args) {
        SpringApplication.run(BemajorApplication.class, args);
    }

    @Bean
    public ApplicationRunner runner(FriendRepository friendRepo, UserRepository userRepository) throws Exception {
        return (args) -> {
            userRepository.save(UserEntity.builder().belong("수원대학교").department("경제학과").userName("강동원").build());
            userRepository.save(UserEntity.builder().belong("수원대학교").department("기계학과").userName("원빈").build());
            friendRepo.save(Friend.builder().id(FriendId.builder().accountId(3L).friendAccountId(5L).build()).build());
            friendRepo.save(Friend.builder().id(FriendId.builder().accountId(3L).friendAccountId(6L).build()).build());
        };
    }
}

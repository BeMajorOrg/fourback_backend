package com.fourback.bemajor.service;

import com.fourback.bemajor.domain.User;
import com.fourback.bemajor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository seniorUserRepository;

    public Optional<User> findByEmail(String email){
        Optional<User> byEmail = seniorUserRepository.findByEmail(email);
        return byEmail;
    }

    public Optional<User> findByOauth2Id(String oauth2Id){
        Optional<User> byOauthId = seniorUserRepository.findByOauth2Id(oauth2Id);
        return byOauthId;
    }

    public void saveSeniorUser(User seniorUser){
        seniorUserRepository.save(seniorUser);
    }
}

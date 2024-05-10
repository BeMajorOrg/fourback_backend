package com.fourback.bemajor.service;

import com.fourback.bemajor.domain.UserDomain;
import com.fourback.bemajor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository seniorUserRepository;

    public Optional<UserDomain> findByEmail(String email){
        Optional<UserDomain> byEmail = seniorUserRepository.findByEmail(email);
        return byEmail;
    }

    public Optional<UserDomain> findByOauth2Id(String oauth2Id){
        Optional<UserDomain> byOauthId = seniorUserRepository.findByOauth2Id(oauth2Id);
        return byOauthId;
    }

    public void saveSeniorUser(UserDomain seniorUser){
        seniorUserRepository.save(seniorUser);
    }
}

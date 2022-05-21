package com.rtyapaev.micronaut.service;

import com.rtyapaev.micronaut.exception.UserAlreadyExistsException;
import com.rtyapaev.micronaut.model.entity.UserEntity;
import com.rtyapaev.micronaut.repository.UserRepository;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Mono<UserEntity> getUserByMsisdn(String msisdn) {
        return userRepository.find(msisdn);
    }

    public Publisher<UserEntity> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Mono<UserEntity> saveUser(String msisdn, String password) {
        return getUserByMsisdn(msisdn)
                .doOnSuccess(userEntity -> {
                    if (userEntity != null) throw new UserAlreadyExistsException(msisdn);
                })
                .switchIfEmpty(Mono.fromDirect(userRepository.save(msisdn, password)));
    }
}

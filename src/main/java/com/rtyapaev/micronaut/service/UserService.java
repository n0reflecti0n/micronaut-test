package com.rtyapaev.micronaut.service;

import com.rtyapaev.micronaut.exception.UserAlreadyExistsException;
import com.rtyapaev.micronaut.model.entity.UserEntity;
import com.rtyapaev.micronaut.repository.UserRepository;

import org.reactivestreams.Publisher;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

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
                .doOnNext(__ -> Mono.error(new UserAlreadyExistsException(msisdn)))
                .switchIfEmpty(Mono.fromDirect(userRepository.save(msisdn, password)));
    }
}

package com.rtyapaev.micronaut.service;

import com.rtyapaev.micronaut.exception.UserAlreadyExistsException;
import com.rtyapaev.micronaut.model.entity.UserEntity;
import com.rtyapaev.micronaut.repository.UserRepository;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@Singleton
@ExecuteOn(TaskExecutors.IO)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<UserEntity> getUserByMsisdn(String msisdn) {
        return userRepository.find(msisdn);
    }

    public UserEntity saveUser(String msisdn, String password) throws UserAlreadyExistsException {
        getUserByMsisdn(msisdn)
                .ifPresent(
                        user -> {
                            throw new UserAlreadyExistsException(String.format("User with msisdn %s already exists", msisdn));
                        }
                );

        final var userEntity = userRepository.save(msisdn, password);
        log.info("Saved new user: {}", userEntity);

        return userEntity;
    }
}

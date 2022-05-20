package com.rtyapaev.micronaut.service;

import com.rtyapaev.micronaut.model.entity.UserEntity;
import com.rtyapaev.micronaut.repository.UserRepository;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
@ExecuteOn(TaskExecutors.IO)
public class UserService {
    @Inject
    private UserRepository userRepository;

    public Optional<UserEntity> getUserByMsisdn(String msisdn) {
        return userRepository.find(msisdn);
    }
}

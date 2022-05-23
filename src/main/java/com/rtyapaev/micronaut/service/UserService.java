package com.rtyapaev.micronaut.service;

import com.rtyapaev.micronaut.exception.UserAlreadyExistsException;
import com.rtyapaev.micronaut.kafka.UserSubscriptionProducer;
import com.rtyapaev.micronaut.model.SubscriptionStatus;
import com.rtyapaev.micronaut.model.dto.UserSubscriptionDto;
import com.rtyapaev.micronaut.model.entity.UserEntity;
import com.rtyapaev.micronaut.repository.UserRepository;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

@Singleton
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserSubscriptionProducer userSubscriptionProducer;

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

    public Mono<Void> updateSubscription(String msisdn, Long subscriptionId, SubscriptionStatus status) {
        return Mono.just(new UserSubscriptionDto(msisdn, subscriptionId, status))
                .flatMap(userSubscriptionDto ->
                        Mono.fromRunnable(() -> userSubscriptionProducer.sendUserSubscription(userSubscriptionDto))
                );
    }
}

package com.rtyapaev.micronaut.service;

import com.rtyapaev.micronaut.model.dto.SubscriptionDto;
import com.rtyapaev.micronaut.model.entity.SubscriptionEntity;
import com.rtyapaev.micronaut.model.entity.UserEntity;
import com.rtyapaev.micronaut.repository.SubscriptionRepository;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.attribute.UserPrincipalNotFoundException;

@Singleton
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserService userService;

    public Flux<SubscriptionDto> getSubscriptions(String msisdn) {
        return subscriptionRepository.findByUserMsisdn(msisdn);
    }

    public Flux<SubscriptionEntity> getSubscriptionEntities(String msisdn) {
        return userService.getUserByMsisdn(msisdn)
                .switchIfEmpty(Mono.error(() -> new UserPrincipalNotFoundException("")))
                .flatMapIterable(UserEntity::subscriptions);
    }
}

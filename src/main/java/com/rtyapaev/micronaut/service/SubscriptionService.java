package com.rtyapaev.micronaut.service;

import com.rtyapaev.micronaut.model.SubscriptionStatus;
import com.rtyapaev.micronaut.model.dto.SubscriptionDto;
import com.rtyapaev.micronaut.model.entity.SubscriptionEntity;
import com.rtyapaev.micronaut.model.entity.UserEntity;
import com.rtyapaev.micronaut.repository.SubscriptionRepository;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Objects;

@Slf4j
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

    public Mono<Void> updateSubscriptionStatus(String msisdn, Long subscriptionId, SubscriptionStatus status) {
        return userService.getUserByMsisdn(msisdn)
                .switchIfEmpty(Mono.error(() -> new UserPrincipalNotFoundException("")))
                .flatMap(user -> updateSubscriptionStatus(user, subscriptionId, status));
    }


    public Mono<Void> updateSubscriptionStatus(UserEntity user, Long subscriptionId, SubscriptionStatus status) {
        return Flux.fromIterable(user.subscriptions())
                .filter(subscriptionEntity -> Objects.equals(subscriptionEntity.subscriptionId(), subscriptionId))
                .next()
                .flatMap(subscriptionEntity -> subscriptionEntity.status() == status
                        ? Mono.error(() -> new HttpStatusException(HttpStatus.CONFLICT, "subscription already has corresponding status"))
                        : Mono.just(subscriptionEntity))
                .flatMap(subscriptionEntity -> updateSubscriptionEntity(subscriptionEntity, status))
                .switchIfEmpty(createSubscription(user, subscriptionId, status).thenReturn(true))
                .then();
    }

    private Mono<Boolean> updateSubscriptionEntity(SubscriptionEntity subscriptionEntity, SubscriptionStatus status) {
        return subscriptionRepository.update(subscriptionEntity.id(), status)
                .doOnSuccess(__ -> log.info(
                        "Msisdn: {} subscription: {} status: {} update successful",
                        subscriptionEntity.user().msisdn(),
                        subscriptionEntity.subscriptionId(),
                        status
                ))
                .thenReturn(true);
    }

    private Mono<SubscriptionEntity> createSubscription(UserEntity user, Long subscriptionId, SubscriptionStatus status) {
        return subscriptionRepository.save(subscriptionId, user, status)
                .doOnNext(__ -> log.info("Created subscription {} for user: {} with status: {}", subscriptionId, user.msisdn(), status));
    }
}

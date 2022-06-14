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
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserService userService;

    private final static Random random = new Random();

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
                .switchIfEmpty(Mono.defer(() -> createSubscription(user, subscriptionId, status).map(__ -> true)))
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
                .then(Mono.just(true));
    }

    public Flux<SubscriptionEntity> subscribeAll(String msisdn) {
        return userService.getUserByMsisdn(msisdn)
                .switchIfEmpty(Mono.error(() -> new UserPrincipalNotFoundException("")))
                .flatMapMany(this::subscribeAll);
    }

    private Mono<SubscriptionEntity> createSubscription(UserEntity user, Long subscriptionId, SubscriptionStatus status) {
        return subscriptionRepository.save(subscriptionId, user, status)
                .doOnNext(__ -> log.info("Created subscription {} for user: {} with status: {}", subscriptionId, user.msisdn(), status));
    }

    private Flux<SubscriptionEntity> subscribeAll(UserEntity user) {
        return Flux.fromIterable(List.of(1L, 2L, 3L))
                .flatMap(subId -> subscribeWithDelay(user, subId));
    }

    private Mono<SubscriptionEntity> subscribeWithDelay(UserEntity user, Long subscriptionId) {
        return subscriptionRepository.save(subscriptionId, user, SubscriptionStatus.ACTIVE)
                .delayElement(Duration.ofSeconds(random.nextInt(5, 11)));
    }
}

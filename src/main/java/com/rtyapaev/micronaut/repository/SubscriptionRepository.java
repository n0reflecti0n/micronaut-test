package com.rtyapaev.micronaut.repository;

import com.rtyapaev.micronaut.model.SubscriptionStatus;
import com.rtyapaev.micronaut.model.dto.SubscriptionDto;
import com.rtyapaev.micronaut.model.entity.SubscriptionEntity;
import com.rtyapaev.micronaut.model.entity.UserEntity;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@R2dbcRepository(dialect = Dialect.POSTGRES)
public interface SubscriptionRepository extends ReactiveStreamsCrudRepository<SubscriptionEntity, Long> {
    Flux<SubscriptionDto> findByUserMsisdn(String msisdn);

    Mono<Void> update(@Id Long id, SubscriptionStatus status);

    Mono<SubscriptionEntity> save(Long subscriptionId, UserEntity user, SubscriptionStatus status);
}

package com.rtyapaev.micronaut.repository;

import com.rtyapaev.micronaut.model.dto.SubscriptionDto;
import com.rtyapaev.micronaut.model.entity.SubscriptionEntity;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository;
import reactor.core.publisher.Flux;

@R2dbcRepository(dialect = Dialect.POSTGRES)
public interface SubscriptionRepository extends ReactiveStreamsCrudRepository<SubscriptionEntity, Long> {
    Flux<SubscriptionDto> findByUserMsisdn(String msisdn);
}

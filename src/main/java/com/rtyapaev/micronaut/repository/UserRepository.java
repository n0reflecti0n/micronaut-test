package com.rtyapaev.micronaut.repository;

import com.rtyapaev.micronaut.model.entity.UserEntity;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository;
import reactor.core.publisher.Mono;

@R2dbcRepository(dialect = Dialect.POSTGRES)
public interface UserRepository extends ReactiveStreamsCrudRepository<UserEntity, Long> {
    Mono<UserEntity> find(String msisdn);

    Mono<UserEntity> save(String msisdn, String password);
}

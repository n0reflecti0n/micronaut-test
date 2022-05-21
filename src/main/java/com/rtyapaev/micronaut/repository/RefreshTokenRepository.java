package com.rtyapaev.micronaut.repository;

import com.rtyapaev.micronaut.model.entity.RefreshTokenEntity;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository;

import java.util.Optional;

@R2dbcRepository(dialect = Dialect.POSTGRES)
public interface RefreshTokenRepository extends ReactiveStreamsCrudRepository<RefreshTokenEntity, Long> {
    RefreshTokenEntity save(String msisdn, String refreshToken, Boolean revoked);

    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);
}

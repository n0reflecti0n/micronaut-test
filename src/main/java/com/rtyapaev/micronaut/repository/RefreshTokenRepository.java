package com.rtyapaev.micronaut.repository;

import com.rtyapaev.micronaut.model.entity.RefreshTokenEntity;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@R2dbcRepository
public interface RefreshTokenRepository extends CrudRepository<RefreshTokenEntity, Long> {
    RefreshTokenEntity save(String msisdn, String refreshToken, Boolean revoked);

    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);
}

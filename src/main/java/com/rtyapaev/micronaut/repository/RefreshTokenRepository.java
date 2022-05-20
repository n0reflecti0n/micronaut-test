package com.rtyapaev.micronaut.repository;

import com.rtyapaev.micronaut.model.entity.RefreshTokenEntity;
import com.rtyapaev.micronaut.model.entity.UserEntity;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@JdbcRepository
public interface RefreshTokenRepository extends CrudRepository<RefreshTokenEntity, Long> {
    RefreshTokenEntity save(UserEntity userEntity, String refreshToken, Boolean revoked);

    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);
}

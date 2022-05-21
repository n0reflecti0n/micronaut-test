package com.rtyapaev.micronaut.repository;

import com.rtyapaev.micronaut.model.entity.RefreshTokenEntity;
import com.rtyapaev.micronaut.model.entity.UserEntity;
import io.micronaut.data.annotation.Join;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository;
import org.reactivestreams.Publisher;

import java.util.Optional;

@R2dbcRepository(dialect = Dialect.POSTGRES)
public interface RefreshTokenRepository extends ReactiveStreamsCrudRepository<RefreshTokenEntity, Long> {
    RefreshTokenEntity save(UserEntity user, String refreshToken, Boolean revoked);

    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);

    @Override
    @Join(value = "user")
    Publisher<RefreshTokenEntity> findById(Long id);
}

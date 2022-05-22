package com.rtyapaev.micronaut.model.entity;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.*;

import java.time.LocalDateTime;

@MappedEntity("refresh_token")
@Introspected
public record RefreshTokenEntity(
        @Id @GeneratedValue @Nullable Long id,
        @MappedProperty UserEntity user,
        String refreshToken,
        Boolean revoked,
        @DateCreated @Nullable LocalDateTime dateAdded
) {
}

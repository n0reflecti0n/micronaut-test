package com.rtyapaev.micronaut.model.entity;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.*;

import java.time.LocalDateTime;

@MappedEntity("refresh_token")
public record RefreshTokenEntity(
        @Id @GeneratedValue @Nullable Long id,
        @Relation(value = Relation.Kind.ONE_TO_ONE) UserEntity user,
        String refreshToken,
        Boolean revoked,
        @DateCreated @Nullable LocalDateTime dateAdded
) {

}

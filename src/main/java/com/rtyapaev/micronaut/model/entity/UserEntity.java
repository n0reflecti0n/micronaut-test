package com.rtyapaev.micronaut.model.entity;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.*;

import java.time.LocalDateTime;

@MappedEntity("user")
@Introspected
public record UserEntity(
        @Id @GeneratedValue @Nullable Long id,
        String msisdn,
        String password,
        @DateCreated @Nullable LocalDateTime dateAdded
) {
}

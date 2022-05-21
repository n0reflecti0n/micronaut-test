package com.rtyapaev.micronaut.model.entity;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

import java.time.LocalDateTime;

@MappedEntity(value = "user")
@Introspected
public record UserEntity(
        @Id @GeneratedValue @Nullable Long id,
        String msisdn,
        String password,
        @DateCreated @Nullable LocalDateTime dateAdded
) {
}

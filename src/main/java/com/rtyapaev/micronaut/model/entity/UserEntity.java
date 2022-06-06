package com.rtyapaev.micronaut.model.entity;

import com.rtyapaev.micronaut.service.PasswordConverter;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.*;
import io.micronaut.data.model.DataType;

import java.time.LocalDateTime;

@MappedEntity("user")
@Introspected
public record UserEntity(
        @Id @GeneratedValue @Nullable Long id,
        String msisdn,
        @TypeDef(type = DataType.STRING, converter = PasswordConverter.class) String password,
        @DateCreated @Nullable LocalDateTime dateAdded
) {
}

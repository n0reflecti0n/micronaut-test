package com.rtyapaev.micronaut.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rtyapaev.micronaut.service.PasswordConverter;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.*;
import io.micronaut.data.model.DataType;

import java.time.LocalDateTime;
import java.util.List;

@MappedEntity("user")
@Introspected
public record UserEntity(
        @Id @GeneratedValue @Nullable Long id,
        String msisdn,
        @TypeDef(type = DataType.STRING, converter = PasswordConverter.class) String password,
        @DateCreated @Nullable LocalDateTime dateAdded,
        @Relation(value = Relation.Kind.ONE_TO_MANY, mappedBy = "user") @Nullable @JsonIgnore List<SubscriptionEntity> subscriptions
) {
}

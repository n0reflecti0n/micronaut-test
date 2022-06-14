package com.rtyapaev.micronaut.model.entity;

import com.rtyapaev.micronaut.model.SubscriptionStatus;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.*;
import io.micronaut.data.model.DataType;

import java.time.LocalDateTime;

@MappedEntity("subscription")
@Introspected
public record SubscriptionEntity(
        @Id @GeneratedValue @Nullable Long id,
        Long subscriptionId,
        @TypeDef(type = DataType.STRING) SubscriptionStatus status,
        @Relation(value = Relation.Kind.ONE_TO_ONE) UserEntity user,
        @DateCreated @Nullable LocalDateTime dateAdded
) {
}

package com.rtyapaev.micronaut.model.dto;

import com.rtyapaev.micronaut.model.SubscriptionStatus;
import io.micronaut.core.annotation.Introspected;

import java.time.LocalDateTime;

@Introspected
public record SubscriptionDto(Long subscriptionId, SubscriptionStatus status, LocalDateTime dateAdded) {
}

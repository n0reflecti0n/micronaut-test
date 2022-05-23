package com.rtyapaev.micronaut.model.dto;

import com.rtyapaev.micronaut.model.SubscriptionStatus;
import io.micronaut.core.annotation.Introspected;

@Introspected
public record UserSubscriptionDto(String subscriberMsisdn, Long subscriptionId, SubscriptionStatus status){
}

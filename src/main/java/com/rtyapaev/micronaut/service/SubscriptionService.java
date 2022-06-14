package com.rtyapaev.micronaut.service;

import com.rtyapaev.micronaut.model.dto.SubscriptionDto;
import com.rtyapaev.micronaut.repository.SubscriptionRepository;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Singleton
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    public Flux<SubscriptionDto> getSubscriptions(String msisdn) {
        return subscriptionRepository.findByUserMsisdn(msisdn);
    }
}

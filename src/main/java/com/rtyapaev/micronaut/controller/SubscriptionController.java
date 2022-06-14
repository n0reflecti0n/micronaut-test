package com.rtyapaev.micronaut.controller;

import com.rtyapaev.micronaut.model.SubscriptionStatus;
import com.rtyapaev.micronaut.model.dto.SubscriptionDto;
import com.rtyapaev.micronaut.model.entity.SubscriptionEntity;
import com.rtyapaev.micronaut.service.SubscriptionService;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Controller("/subscription")
@Secured(SecurityRule.IS_AUTHENTICATED)
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @Get
    public Flux<SubscriptionDto> getSubscriptions(Principal principal) {
        return Mono.just(principal.getName())
                .flatMapMany(subscriptionService::getSubscriptions);
    }

    @Get("/entities")
    public Flux<SubscriptionEntity> getSubscriptionEntities(Principal principal) {
        return Mono.just(principal.getName())
                .flatMapMany(subscriptionService::getSubscriptionEntities);
    }

    @Post("/{subscriptionId}")
    public Mono<Void> subscribe(@PathVariable Long subscriptionId, Principal principal) {
        return Mono.just(principal)
                .map(Principal::getName)
                .flatMap(msisdn -> subscriptionService.updateSubscriptionStatus(msisdn, subscriptionId, SubscriptionStatus.ACTIVE));
    }

    @Post("/subscribe/all")
    public Flux<SubscriptionEntity> subscribeAll(Principal principal) {
        return Mono.just(principal)
                .map(Principal::getName)
                .flatMapMany(subscriptionService::subscribeAll);
    }
}

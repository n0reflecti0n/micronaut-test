package com.rtyapaev.micronaut.controller;

import com.rtyapaev.micronaut.model.dto.SubscriptionDto;
import com.rtyapaev.micronaut.model.entity.SubscriptionEntity;
import com.rtyapaev.micronaut.service.SubscriptionService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import java.security.Principal;

@Controller("/subscription")
@Secured(SecurityRule.IS_AUTHENTICATED)
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @Get
    public Flux<SubscriptionDto> getSubscriptions(Principal principal) {
        final var msisdn = principal.getName();
        return subscriptionService.getSubscriptions(msisdn);
    }
}

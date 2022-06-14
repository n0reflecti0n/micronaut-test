package com.rtyapaev.micronaut.controller;

import com.rtyapaev.micronaut.model.SubscriptionStatus;
import com.rtyapaev.micronaut.model.dto.SubscriptionDto;
import com.rtyapaev.micronaut.model.entity.SubscriptionEntity;
import com.rtyapaev.micronaut.service.SubscriptionService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Controller("/subscription")
@Tag(name = "Subscription controls")
@Secured(SecurityRule.IS_AUTHENTICATED)
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @Operation(summary = "Get user subscriptions as DTOs")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "OK"))
    @Get
    public Flux<SubscriptionDto> getSubscriptions(Principal principal) {
        return Mono.just(principal.getName())
                .flatMapMany(subscriptionService::getSubscriptions);
    }

    @Operation(summary = "Get user subscriptions as entities")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "OK"))
    @Get("/entities")
    public Flux<SubscriptionEntity> getSubscriptionEntities(Principal principal) {
        return Mono.just(principal.getName())
                .flatMapMany(subscriptionService::getSubscriptionEntities);
    }

    @Operation(summary = "Subscribe to certain service")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "409", description = "Subscription already active")
    })
    @Post("/{subscriptionId}")
    public Mono<Void> subscribe(@PathVariable Long subscriptionId, Principal principal) {
        return Mono.just(principal)
                .map(Principal::getName)
                .flatMap(msisdn -> subscriptionService.updateSubscriptionStatus(msisdn, subscriptionId, SubscriptionStatus.ACTIVE));
    }
}

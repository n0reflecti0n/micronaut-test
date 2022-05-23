package com.rtyapaev.micronaut.controller;

import com.rtyapaev.micronaut.model.SubscriptionStatus;
import com.rtyapaev.micronaut.model.entity.UserEntity;
import com.rtyapaev.micronaut.service.UserService;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Slf4j
@Controller("/user")
@ExecuteOn(TaskExecutors.IO)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Get
    public Mono<UserEntity> getUser(@Parameter String msisdn) {
        return userService.getUserByMsisdn(msisdn)
                .doOnNext(userEntity -> log.info("User: {}", userEntity))
                .switchIfEmpty(Mono.fromRunnable(() -> log.info("User not found")));
    }

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Get("/{id}")
    public UserEntity getUserById(@PathVariable Long id) {
        return Mono.from(userService.getUserById(id))
                .block();
    }

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Post
    public Mono<UserEntity> saveUser(String msisdn, String password) {
        return userService.saveUser(msisdn, password);
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Post("/subscribe")
    public Mono<Void> updateSubscription(Authentication authentication,
                                         Long subscriptionId,
                                         SubscriptionStatus status) {
        return userService.updateSubscription(authentication.getName(), subscriptionId, status);
    }
}

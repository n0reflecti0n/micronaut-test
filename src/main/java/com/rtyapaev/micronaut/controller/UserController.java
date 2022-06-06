package com.rtyapaev.micronaut.controller;

import com.rtyapaev.micronaut.model.SubscriptionStatus;
import com.rtyapaev.micronaut.model.entity.UserEntity;
import com.rtyapaev.micronaut.service.UserService;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

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
    public Mono<UserEntity> getUserById(@PathVariable Long id) {
        return Mono.from(userService.getUserById(id))
                .switchIfEmpty(Mono.fromRunnable(() -> log.info("User with id: {} doesn't exist", id)));
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

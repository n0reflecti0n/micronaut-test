package com.rtyapaev.micronaut.controller;

import com.rtyapaev.micronaut.model.entity.RefreshTokenEntity;
import com.rtyapaev.micronaut.model.entity.UserEntity;
import com.rtyapaev.micronaut.repository.RefreshTokenRepository;
import com.rtyapaev.micronaut.service.UserService;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Controller("/user")
@ExecuteOn(TaskExecutors.IO)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;

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

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Get("/token")
    public Publisher<RefreshTokenEntity> getRefreshToken(Long id) {
        return refreshTokenRepository.findById(id);
    }
}

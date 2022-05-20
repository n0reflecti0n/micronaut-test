package com.rtyapaev.micronaut.controller;

import com.rtyapaev.micronaut.model.entity.UserEntity;
import com.rtyapaev.micronaut.service.UserService;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@Controller("/user")
@Secured(SecurityRule.IS_ANONYMOUS)
@RequiredArgsConstructor
public class UserController {
    public final UserService userService;

    @Get
    public Optional<UserEntity> getUser(@Parameter String msisdn) {
        final var user = userService.getUserByMsisdn(msisdn);
        log.info("User: {}", user);
        return user;
    }

    @Post
    public UserEntity saveUser(@Parameter String msisdn,
                               @Parameter String password) {
        return userService.saveUser(msisdn, password);
    }
}

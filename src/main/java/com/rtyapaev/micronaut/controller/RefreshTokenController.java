package com.rtyapaev.micronaut.controller;

import com.rtyapaev.micronaut.model.entity.RefreshTokenEntity;
import com.rtyapaev.micronaut.service.RefreshTokenService;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Controller("/token")
@Secured(SecurityRule.IS_AUTHENTICATED)
@RequiredArgsConstructor
public class RefreshTokenController {
    private final RefreshTokenService refreshTokenService;

    @Get
    public Mono<RefreshTokenEntity> getRefreshToken(String msisdn) {
        return refreshTokenService.getByUserMsisdn(msisdn);
    }
}

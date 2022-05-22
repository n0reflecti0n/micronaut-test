package com.rtyapaev.micronaut.controller;

import com.rtyapaev.micronaut.model.entity.RefreshTokenEntity;
import com.rtyapaev.micronaut.service.RefreshTokenService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Controller("/token")
@RequiredArgsConstructor
public class RefreshTokenController {
    private final RefreshTokenService refreshTokenService;

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Get
    public Mono<RefreshTokenEntity> getRefreshToken(String msisdn) {
        return refreshTokenService.getByUserMsisdn(msisdn);
    }
}

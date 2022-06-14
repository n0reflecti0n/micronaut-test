package com.rtyapaev.micronaut.controller;

import com.rtyapaev.micronaut.model.entity.RefreshTokenEntity;
import com.rtyapaev.micronaut.service.RefreshTokenService;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Controller("/token")
@Tag(name = "Refresh token controls")
@Secured(SecurityRule.IS_AUTHENTICATED)
@RequiredArgsConstructor
public class RefreshTokenController {
    private final RefreshTokenService refreshTokenService;

    @Operation(summary = "Get refresh token by user msisdn")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "OK"))
    @Get
    public Mono<RefreshTokenEntity> getRefreshToken(String msisdn) {
        return refreshTokenService.getByUserMsisdn(msisdn);
    }
}

package com.rtyapaev.micronaut.service;

import com.rtyapaev.micronaut.model.entity.RefreshTokenEntity;
import com.rtyapaev.micronaut.repository.RefreshTokenRepository;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Singleton
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public Mono<RefreshTokenEntity> getByUserMsisdn(String msisdn) {
        return refreshTokenRepository.findByUserMsisdn(msisdn);
    }
}

package com.rtyapaev.micronaut.service;

import com.rtyapaev.micronaut.model.entity.RefreshTokenEntity;
import com.rtyapaev.micronaut.repository.RefreshTokenRepository;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public Mono<RefreshTokenEntity> getByUserMsisdn(String msisdn) {
        return refreshTokenRepository.findByUserMsisdn(msisdn);
    }
}

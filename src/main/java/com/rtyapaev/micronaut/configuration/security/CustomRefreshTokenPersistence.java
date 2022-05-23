package com.rtyapaev.micronaut.configuration.security;

import static io.micronaut.security.errors.IssuingAnAccessTokenErrorCode.INVALID_GRANT;

import java.util.Objects;

import javax.transaction.Transactional;

import com.rtyapaev.micronaut.model.entity.RefreshTokenEntity;
import com.rtyapaev.micronaut.repository.RefreshTokenRepository;
import com.rtyapaev.micronaut.service.UserService;

import org.reactivestreams.Publisher;

import io.micronaut.core.util.StringUtils;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.errors.OauthErrorResponseException;
import io.micronaut.security.token.event.RefreshTokenGeneratedEvent;
import io.micronaut.security.token.refresh.RefreshTokenPersistence;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Singleton
@RequiredArgsConstructor
public class CustomRefreshTokenPersistence implements RefreshTokenPersistence {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    @Override
    @Transactional
    public void persistToken(RefreshTokenGeneratedEvent event) {
        Mono.just(event)
                .filter(Objects::nonNull)
                .filter(tokenGeneratedEvent -> tokenGeneratedEvent.getRefreshToken() != null)
                .map(RefreshTokenGeneratedEvent::getAuthentication)
                .filter(Objects::nonNull)
                .map(Authentication::getName)
                .filter(StringUtils::isNotEmpty)
                .flatMap(userService::getUserByMsisdn)
                .map(userEntity ->
                        new RefreshTokenEntity(null, userEntity, event.getRefreshToken(), false, null)
                )
                .flatMap(refreshTokenEntity -> Mono.from(refreshTokenRepository.save(refreshTokenEntity)))
                .subscribe();
    }

    @Override
    public Publisher<Authentication> getAuthentication(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .switchIfEmpty(Mono.error(new OauthErrorResponseException(INVALID_GRANT, "refresh token not found", null)))
                .filter(refreshTokenEntity -> !refreshTokenEntity.revoked())
                .map(token -> Authentication.build(token.user().msisdn()))
                .switchIfEmpty(Mono.error(new OauthErrorResponseException(INVALID_GRANT, "refresh token revoked", null)));
    }
}

package com.rtyapaev.micronaut.configuration.security;

import com.rtyapaev.micronaut.model.entity.RefreshTokenEntity;
import com.rtyapaev.micronaut.repository.RefreshTokenRepository;
import com.rtyapaev.micronaut.service.UserService;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.errors.OauthErrorResponseException;
import io.micronaut.security.token.event.RefreshTokenGeneratedEvent;
import io.micronaut.security.token.refresh.RefreshTokenPersistence;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import static io.micronaut.security.errors.IssuingAnAccessTokenErrorCode.INVALID_GRANT;

@Singleton
@RequiredArgsConstructor
public class CustomRefreshTokenPersistence implements RefreshTokenPersistence {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    @Override
    public void persistToken(RefreshTokenGeneratedEvent event) {
        if (event != null && event.getRefreshToken() != null && event.getAuthentication() != null) {
            final var msisdn = event.getAuthentication().getName();
            if (msisdn != null) {
                userService.getUserByMsisdn(msisdn)
                        .ifPresent(userEntity ->
                                refreshTokenRepository.save(userEntity, event.getRefreshToken(), false)
                        );
            }
        }
    }

    @Override
    public Publisher<Authentication> getAuthentication(String refreshToken) {
        return Flux.create(
                emitter -> refreshTokenRepository.findByRefreshToken(refreshToken)
                        .ifPresentOrElse(
                                token -> updateToken(emitter, token),
                                () -> emitter.error(
                                        new OauthErrorResponseException(INVALID_GRANT, "refresh token not found", null)
                                )

                        ),
                FluxSink.OverflowStrategy.ERROR
        );
    }

    private void updateToken(FluxSink<Authentication> emitter, RefreshTokenEntity token) {
        if (token.revoked()) {
            emitter.error(new OauthErrorResponseException(INVALID_GRANT, "refresh token revoked", null));
        } else {
            emitter.next(Authentication.build(token.user().msisdn()));
            emitter.complete();
        }
    }
}

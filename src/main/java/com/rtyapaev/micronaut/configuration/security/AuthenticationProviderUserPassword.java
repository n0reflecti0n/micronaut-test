package com.rtyapaev.micronaut.configuration.security;

import com.rtyapaev.micronaut.model.entity.UserEntity;
import com.rtyapaev.micronaut.service.UserService;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.Optional;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class AuthenticationProviderUserPassword implements AuthenticationProvider {
    private final UserService userService;

    @Override
    public Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest,
                                                          AuthenticationRequest<?, ?> authenticationRequest) {

        return Flux.create(
                emitter -> authorize(authenticationRequest, emitter),
                FluxSink.OverflowStrategy.ERROR
        );
    }

    private void authorize(AuthenticationRequest<?, ?> authenticationRequest,
                           FluxSink<AuthenticationResponse> emitter) {
        final var msisdn = (String) authenticationRequest.getIdentity();
        final var password = (String) authenticationRequest.getSecret();

        Optional.ofNullable(userService.getUserByMsisdn(msisdn).block())
                .ifPresentOrElse(
                        userEntity -> validatePassword(emitter, userEntity, password),
                        () -> emitter.error(AuthenticationResponse.exception())
                );
    }

    private void validatePassword(FluxSink<AuthenticationResponse> emitter,
                                  UserEntity userEntity,
                                  String password) {
        final var msisdn = userEntity.msisdn();
        if (userEntity.password().equals(password)) {
            log.info("Msisdn: {} authentication successful", msisdn);
            emitter.next(AuthenticationResponse.success(msisdn));
            emitter.complete();
        } else {
            log.error("Msisdn: {} - wrong password", msisdn);
            emitter.error(AuthenticationResponse.exception());
        }
    }
}

package com.rtyapaev.micronaut.security;

import com.rtyapaev.micronaut.model.entity.UserEntity;
import com.rtyapaev.micronaut.service.UserService;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

@Slf4j
@Singleton
public class AuthenticationProviderUserPassword implements AuthenticationProvider {
    @Inject
    private UserService userService;

    @Override
    public Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest,
                                                          AuthenticationRequest<?, ?> authenticationRequest) {

        return Flux.create(emitter -> {
            final var msisdn = (String) authenticationRequest.getIdentity();
            final var password = (String) authenticationRequest.getSecret();

            log.info("test");

            userService.getUserByMsisdn(msisdn)
                    .ifPresentOrElse(
                            userEntity -> validatePassword(emitter, userEntity, password),
                            () -> emitter.error(AuthenticationResponse.exception())
                    );
        }, FluxSink.OverflowStrategy.ERROR);
    }

    private void validatePassword(FluxSink<AuthenticationResponse> emitter,
                                  UserEntity userEntity,
                                  String password) {
        if (userEntity.getPassword().equals(password)) {
            emitter.next(AuthenticationResponse.success(userEntity.getMsisdn()));
            emitter.complete();
        } else {
            emitter.error(AuthenticationResponse.exception());
        }
    }
}
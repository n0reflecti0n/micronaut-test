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
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class AuthenticationProviderUserPassword implements AuthenticationProvider {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest,
                                                          AuthenticationRequest<?, ?> authenticationRequest) {
        return Mono.just((String) authenticationRequest.getIdentity())
                .flatMap(userService::getUserByMsisdn)
                .switchIfEmpty(Mono.error(AuthenticationResponse.exception("User not found")))
                .filter(userEntity -> passwordEncoder.matches(authenticationRequest.getSecret().toString(), userEntity.password()))
                .map(UserEntity::msisdn)
                .doOnNext(msisdn -> log.info("User: {} authentication successful", msisdn))
                .map(AuthenticationResponse::success)
                .switchIfEmpty(Mono.error(AuthenticationResponse.exception("Invalid password")));
    }
}

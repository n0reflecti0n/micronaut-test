package com.rtyapaev.micronaut.exception;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Produces
@Singleton
@Requires(classes = {UserAlreadyExistsException.class, ExceptionHandler.class})
public class UserAlreadyExistsExceptionHandler implements ExceptionHandler<UserAlreadyExistsException, HttpResponse<?>> {

    @Override
    public HttpResponse<?> handle(HttpRequest request, UserAlreadyExistsException exception) {
        log.error(exception.getMessage());
        return HttpResponse.status(HttpStatus.CONFLICT);
    }
}

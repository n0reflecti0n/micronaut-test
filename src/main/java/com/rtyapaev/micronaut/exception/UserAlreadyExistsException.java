package com.rtyapaev.micronaut.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String msisdn) {
        super(String.format("User with msisdn %S already exists", msisdn));
    }
}

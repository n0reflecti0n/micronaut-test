package com.rtyapaev.micronaut.configuration;

import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Factory
public class BeanFactory {
    @Singleton
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
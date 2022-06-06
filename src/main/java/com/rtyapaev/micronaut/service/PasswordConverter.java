package com.rtyapaev.micronaut.service;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.convert.ConversionContext;
import io.micronaut.data.model.runtime.convert.AttributeConverter;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Singleton
@RequiredArgsConstructor
public class PasswordConverter implements AttributeConverter<String, String> {
    private final PasswordEncoder passwordEncoder;

    @Override
    public String convertToPersistedValue(String entityValue, @NonNull ConversionContext context) {
        return passwordEncoder.encode(entityValue);
    }

    @Override
    public String convertToEntityValue(String persistedValue, @NonNull ConversionContext context) {
        return persistedValue;
    }
}

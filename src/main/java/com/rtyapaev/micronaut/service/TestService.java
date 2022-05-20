package com.rtyapaev.micronaut.service;

import com.rtyapaev.micronaut.model.dto.HelloDto;
import jakarta.inject.Singleton;

@Singleton
public class TestService {

    public HelloDto getHelloString() {
        return new HelloDto("hello");
    }
}

package com.rtyapaev.micronaut.controller;

import com.rtyapaev.micronaut.model.dto.HelloDto;
import com.rtyapaev.micronaut.service.TestService;
import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;

import java.security.Principal;

@Slf4j
@Controller("/test")
@Secured(SecurityRule.IS_ANONYMOUS)
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class TestController {
    public final TestService testService;

    @Get
    public Publisher<HelloDto> getHello(Principal principal) {
        log.info("Principal: {}", principal);
        return new Publishers.JustPublisher<>(testService.getHelloString());
    }
}

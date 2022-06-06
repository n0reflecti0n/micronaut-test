package com.rtyapaev.micronaut;

import io.micronaut.openapi.annotation.OpenAPIInclude;
import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.slf4j.bridge.SLF4JBridgeHandler;

@OpenAPIDefinition(
        info = @Info(
                title = "Micronaut test",
                version = "0.1",
                description = "Test application for micronaut learning"
        )
)
@OpenAPIInclude(
        classes = {
                io.micronaut.security.endpoints.LoginController.class,
                io.micronaut.security.endpoints.LogoutController.class
        }
)
public class Application {
    public static void main(String[] args) {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        Micronaut.build(args)
                .eagerInitSingletons(true)
                .mainClass(Application.class)
                .start();
    }
}

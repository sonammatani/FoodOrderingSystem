package com.project.app.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API Documentation",
                version = "v1",
                description = "API documentation for the application"
        )
)
public class SwaggerConfiguration {
    // Additional configuration if necessary
}

package com.project.app.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "FoodMania API Documentation",
                version = "v1",
                description = "API documentation for the application : FoodMania"
        )
)
public class SwaggerConfiguration {
}

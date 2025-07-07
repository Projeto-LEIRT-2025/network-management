package com.github.project.webapplication.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenAPIConfig {

    @Bean
    fun customOpenAPI() = OpenAPI()
        .info(
            Info()
                .title("Network Management API")
                .version("v1.0.0")
                .description("API documentation for device management and monitoring. This API provides endpoints for configuring network devices, managing their interfaces and services, as well as retrieving operational metrics and statistics to support network monitoring and analysis.")
        )
        .components(
            Components().addSecuritySchemes(
                "bearerAuth",
                SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
            )
        )
}
package com.ticketjo.ticketjo_backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	OpenAPI ticketjoOpenAPI() {
		return new OpenAPI()
				.info(new Info().title("Ticketjo API")
				.description("Documentation de l'API du projet Ticketjo")
					.version("1.0")
					.license(new License().name("JO.")));
	}
}
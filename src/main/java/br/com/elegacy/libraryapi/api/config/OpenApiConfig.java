package br.com.elegacy.libraryapi.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

	@Bean
	OpenAPI springShopOpenAPI() {
		return new OpenAPI()
				.info(info());
	}

	private Info info() {
		return new Info().title("Library API")
				.description("Book Control Project API")
				.version("v0.0.1")
				.license(license())
				.contact(contact());
	}

	private License license() {
		return new License().name("Apache 2.0").url("http://springdoc.org");
	}

	private Contact contact() {
		return new Contact()
				.email("leandrosouza.dev@gmail.com")
				.name("Leandro Souza")
				.url("https://github.com/leandrosouzadev");
	}
}

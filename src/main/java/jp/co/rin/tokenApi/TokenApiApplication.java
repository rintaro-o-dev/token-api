package jp.co.rin.tokenApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import jp.co.rin.tokenApi.config.ParameterLoader;

@SpringBootApplication
@OpenAPIDefinition(servers = {@Server(url = "${openapi.server-url}")})
public class TokenApiApplication {

	public static void main(String[] args) {

		// Load SSM parameters before starting Spring Boot
		ParameterLoader.load();

		SpringApplication.run(TokenApiApplication.class, args);
	}

}

package jp.co.rin.tokenApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;

@SpringBootApplication
@OpenAPIDefinition(servers = {@Server(url = "${openapi.server-url}")})
public class TokenApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TokenApiApplication.class, args);
	}

}

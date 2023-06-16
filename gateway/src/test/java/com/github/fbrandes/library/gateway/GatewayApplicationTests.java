package com.github.fbrandes.library.gateway;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GatewayApplicationTests {
	@Autowired
	private WebTestClient client;

	@BeforeEach
	void setup(ApplicationContext context) {
		client = WebTestClient.bindToApplicationContext(context).build();
	}
	@ParameterizedTest
	@ValueSource(strings = {"info", "health", "gateway/routes"})
	void actuatorEndpointsAvailable(String endpoint) {
		client.get()
			.uri("/actuator/" + endpoint)
			.exchange()
			.expectStatus()
			.isOk();
	}

}

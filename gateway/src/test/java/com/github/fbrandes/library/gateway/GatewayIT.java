package com.github.fbrandes.library.gateway;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
@ContextConfiguration(
    initializers = GatewayIT.GatewayPropertiesInitializer.class,
    classes = GatewayApplication.class)
@Testcontainers
public class GatewayIT {
    @Container
    private static final MockServerContainer mockServer = new MockServerContainer(DockerImageName
            .parse("mockserver/mockserver")
            .withTag("mockserver-" + MockServerClient.class.getPackage().getImplementationVersion()))
        .withExposedPorts(1080);

    @Autowired
    private WebTestClient webTestClient;

    private static MockServerClient mockClient;

    private static final String ID = "a3c5ecf3-ca19-4d4e-a9be-4f4142f37460";

    @BeforeAll
    static void prepareMocks() {
        mockClient = new MockServerClient(mockServer.getHost(), mockServer.getServerPort());
        mockClient
            .when(
                request()
                    .withMethod("GET")
                    .withPath("/books/" + ID))
            .respond(
                response()
                    .withStatusCode(200)
                    .withBody("{ \"id\": \"a3c5ecf3-ca19-4d4e-a9be-4f4142f37460\", \"title\": \"Some Book\"," +
                        " \"isbn\": \"978-0-201-83595-3\"}")
            );
    }

    @Test
    void shouldFindBookSuccessfully() {
        webTestClient.get()
            .uri("/books/{id}", ID)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody()
            .json("{ \"id\": \"a3c5ecf3-ca19-4d4e-a9be-4f4142f37460\", \"title\": \"Some Book\"," +
                " \"isbn\": \"978-0-201-83595-3\"}");
    }

    static class GatewayPropertiesInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize (@NotNull ConfigurableApplicationContext configurableApplicationContext){
            String mockServerUri = String.format("http://%s:%d", mockServer.getHost(), mockServer.getServerPort());

            TestPropertyValues.of("BOOKINFO_URI=" + mockServerUri).applyTo(configurableApplicationContext);
        }
    }

    @AfterAll
    static void tearDown() {
        mockClient.stop();
    }
}

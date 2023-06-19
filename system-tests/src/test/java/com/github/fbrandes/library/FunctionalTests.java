package com.github.fbrandes.library;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.nio.file.Paths;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

@Testcontainers
class FunctionalTests {
    @Container
    private final ElasticsearchContainer elasticsearchContainer = new ElasticsearchContainer(
            DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch:7.17.0"));

    @Container
    private final GenericContainer<?> bookInfoServiceContainer = new GenericContainer<>(
        new ImageFromDockerfile().withDockerfile(
            Paths.get("../bookinfo/bookinfo-service/src/main/docker/Dockerfile.jvm")))
        .dependsOn(elasticsearchContainer);

    @Container
    private final GenericContainer<?> gatewayContainer = new GenericContainer<>(
        new ImageFromDockerfile().withDockerfile(
            Paths.get("../gateway/Dockerfile")))
        .dependsOn(bookInfoServiceContainer);

    @Test
    void shouldReadAndWriteBooksSuccessfully() {
        TestBook book1 = new TestBook();
        book1.setId("a3c5ecf3-ca19-4d4e-a9be-4f4142f37460");
        book1.setTitle("My Book");
        book1.setIsbn("978-0-201-83595-3");

        // create book
        List.of(book1).forEach(b -> given()
            .when()
            .post("/books", b)
            .then()
            .statusCode(201)
            .body(is("/books/" + b.getId())));

        // search for created book
        given()
            .when()
            .get("/books/1")
            .then()
            .statusCode(200)
            .body(contains("\"id\": \"a3c5ecf3-ca19-4d4e-a9be-4f4142f37460\""))
            .and().body(contains("\"title\": \"My Book\""))
            .and().body(contains("\"isbn\": \"978-0-201-83595-3\""));

        // delete found book
        given()
            .when()
            .delete("/books/1")
            .then()
            .statusCode(200);
    }

    /*
      Test paging
        create many books
        make a search with many results

      Later:
        Add Toxiproxy for testing system reponse during networking issues
     */
}
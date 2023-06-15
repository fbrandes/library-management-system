package com.github.fbrandes.library.bookinfo.controller;

import com.github.fbrandes.library.bookinfo.model.Author;
import com.github.fbrandes.library.bookinfo.model.Book;
import com.google.gson.Gson;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@QuarkusTest
@Testcontainers
class BookInfoServiceIT {
    @Test
    void testHelloEndpoint() {
//        given()
//                .when().get("/hello")
//                .then()
//                .statusCode(200)
//                .body(is("Hello Books!"));
    }


    @Test
    void testList() {
//        given()
//                .when().get("/fruits")
//                .then()
//                .statusCode(200)
//                .body("$.size()", is(2),
//                        "name", containsInAnyOrder("Apple", "Pineapple"),
//                        "description", containsInAnyOrder("Winter fruit", "Tropical fruit"));
    }

    @Test
    void shouldIndexBookSuccessfully() {
        String id = UUID.randomUUID().toString();

        Book book = new Book();
        book.setId(id);
        book.setAuthors(List.of(new Author("", "Test Author", Collections.emptyList())));
        book.setTitle("Some Book");

        String body = new Gson().toJson(book);

        Response response =
                given()
                        .body(body)
                        .header("Content-Type", MediaType.APPLICATION_JSON)
                        .when()
                        .post("/books");

        String location = response.getHeader("Location");
        Assertions.assertNotNull(location);

        String createdUuid = Arrays.stream(location.split("/")).reduce((first, second) -> second).orElse(null);

        Assertions.assertEquals(id, createdUuid);
        Assertions.assertEquals(201, response.getStatusCode());
    }

    @Test
    void shouldIndexBookSuccessfullyWithoutBookId() {
        Book book = new Book();
        book.setAuthors(List.of(new Author("", "Test Author", Collections.emptyList())));
        book.setTitle("Some Book");

        String body = new Gson().toJson(book);

        Response response =
                given()
                        .body(body)
                        .header("Content-Type", MediaType.APPLICATION_JSON)
                        .when()
                        .post("/books");

        String location = response.getHeader("Location");
        Assertions.assertNotNull(location);
        Assertions.assertEquals(201, response.getStatusCode());

        String createdUuid = Arrays.stream(location.split("/")).reduce((first, second) -> second).orElse(null);
        Assertions.assertNotNull(createdUuid);
    }

    private void createBookData() {

    }

    @AfterEach
    void resetData() {
        // delete all data from index
    }
}
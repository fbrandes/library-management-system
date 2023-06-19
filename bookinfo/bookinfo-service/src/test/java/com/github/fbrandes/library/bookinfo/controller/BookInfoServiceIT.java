package com.github.fbrandes.library.bookinfo.controller;

import com.github.fbrandes.library.bookinfo.model.Author;
import com.github.fbrandes.library.bookinfo.model.Book;
import com.google.gson.Gson;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusIntegrationTest
@Testcontainers
class BookInfoServiceIT {

    @Test
    void shouldCreateBookSuccessfully() {
        Book book = createBookData(UUID.randomUUID().toString());

        Response response =
                given()
                        .body(book)
                        .header("Content-Type", MediaType.APPLICATION_JSON)
                        .when()
                        .post("/books");

        String location = response.getHeader("Location");
        assertNotNull(location);

        String createdUuid = Arrays.stream(location.split("/")).reduce((first, second) -> second).orElse(null);

        assertEquals(book.getId(), createdUuid);
        assertEquals(201, response.getStatusCode());
    }

    @Test
    void shouldCreateBookSuccessfullyWithoutBookId() {
        Book book = createBookData(null);

        Response response =
                given()
                        .body(book)
                        .header("Content-Type", MediaType.APPLICATION_JSON)
                        .when()
                        .post("/books");

        String location = response.getHeader("Location");
        assertNotNull(location);
        assertEquals(201, response.getStatusCode());

        String createdUuid = Arrays.stream(location.split("/")).reduce((first, second) -> second).orElse(null);
        assertNotNull(createdUuid);
    }

    @Test
    void shouldFetchBookSuccessfully() {
        Book book = createBookData(UUID.randomUUID().toString());

        Response response =
            given()
                .body(book)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .post("/books");

        assertEquals(201, response.getStatusCode());

        response =
            given()
                .when()
                .get("/books/" + book.getId());

        assertEquals(200, response.getStatusCode());
        assertEquals(book.getId(), response.getBody().as(Book.class).getId());
    }

    @Test
    void shouldUpdateBookSuccessfully() {
        Book book = createBookData(UUID.randomUUID().toString());

        Response response =
            given()
                .body(book)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .post("/books");

        assertEquals(201, response.getStatusCode());

        book.setTitle("Updated Title");

        response =
            given()
                .body(book)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .put("/books");

        assertEquals(200, response.getStatusCode());
        assertEquals(book.getTitle(), response.getBody().as(Book.class).getTitle());
    }

    @Test
    void shouldDeleteBookSuccessfully() {
        Book book = createBookData(UUID.randomUUID().toString());

        Response response =
            given()
                .body(book)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .post("/books");

        assertEquals(201, response.getStatusCode());

        response =
            given()
                .body(book)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .delete("/books/" + book.getId());

        assertEquals(204, response.getStatusCode());

    }

    private Book createBookData(String id) {
        Book book = new Book();
        book.setId(id);
        book.setAuthors(List.of(new Author("", "Test Author", Collections.emptyList())));
        book.setTitle("Some Book");
        return book;
    }
}
package com.github.fbrandes.library;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class BookInfoPagingTests {
    @BeforeAll
    static void prepareData() {
        // create books
        for (int i = 0; i < 100; i++) {
            TestBook book = new TestBook();
            book.setId(String.valueOf(i));
            book.setTitle("Book" + i);
            book.setIsbn("978-" + i);

            given()
                .when()
                .post("/books", book)
                .then()
                .statusCode(201)
                .body(is("/books/" + book.getId()));
        }
    }

    @Test
    void shouldApplyPagingSuccessfully() {
        // query books
        given()
            .when()
            .get("/books")
            .then()
            .statusCode(200);
        // TODO check if body contains page size
        // check page is 0
    }

    @Test
    void shouldReadAnyPage() {
        // query books
        given()
            .when()
            .get("/books")
            .then()
            .statusCode(200);
        // TODO check if body contains page size
        // check page is 0

        // read next page
        // check data & page
    }

    @Test
    void shouldReadAllyPages() {
        // query books
        given()
            .when()
            .get("/books")
            .then()
            .statusCode(200);
        // TODO check if body contains page size
        // check page is 0

        // read til end
        // do checks
    }
}

package com.github.fbrandes.library.bookinfo;

import com.github.fbrandes.library.bookinfo.controller.BookResourceDto;
import com.github.fbrandes.library.bookinfo.model.Book;
import com.github.fbrandes.library.bookinfo.model.Isbn;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@QuarkusTest
class BookInfoPagingIT {
    private List<String> bookIds;

    @BeforeEach
    void prepareData() {
        bookIds = new ArrayList<>();

        // create books
        for (int i = 1; i <= 100; i++) {
            String id = UUID.randomUUID().toString();
            Book book = new Book();
            book.setId(id);
            book.setTitle("Book" + i);
            book.setIsbn(List.of(new Isbn(Isbn.Type.ISBN_13, "978-" + i)));

            given()
                    .when()
                    .body(book)
                    .header("Content-Type", MediaType.APPLICATION_JSON)
                    .post("/books")
                    .then()
                    .statusCode(201)
                    .header(HttpHeaders.LOCATION, "http://localhost:8081/books/" + id)
                    .body(is(emptyString()));
            bookIds.add(id);
        }

        waitForDocumentsBeingIndexed();
    }

    @Test
    void shouldApplyPagingSuccessfully() {
        // query books
        Response response = given()
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get("/books")
                .then()
                .statusCode(200)
                .extract()
                .response();

        BookResourceDto bookDto = response.getBody().as(new TypeRef<>() {/**/
        });
        // TODO check if body contains page size
        // check page is 0
        assertEquals(10, bookDto.getBooks().size());
        assertEquals("Book1", bookDto.getBooks().get(0).getTitle());
    }

    @Test
    void shouldReadAnyPage() {
        // query books
        Response response = given()
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get("/books")
                .then()
                .statusCode(200)
                .extract()
                .response();
        // TODO check if body contains page size
        // check page is 0
        BookResourceDto bookDto = response.getBody().as(new TypeRef<>() {/**/
        });
        // TODO check if body contains page size
        // check page is 0
        assertEquals(10, bookDto.getBooks().size());
        assertEquals("Book1", bookDto.getBooks().get(0).getTitle());
        assertEquals(0, bookDto.getPage().getNumber());
        assertEquals(100, bookDto.getPage().getTotal());
        assertEquals(10, bookDto.getPage().getSize());

        // read next page
        response = given()
                .queryParam("page", 1)
                .queryParam("size", 10)
                .when()
                .get("/books")
                .then()
                .statusCode(200)
                .extract()
                .response();

        // check data & page
        bookDto = response.getBody().as(new TypeRef<>() {/**/
        });
        // TODO check if body contains page size
        // check page is 0
        assertEquals(10, bookDto.getBooks().size());
        assertEquals("Book11", bookDto.getBooks().get(0).getTitle());
        assertEquals(1, bookDto.getPage().getNumber());
        assertEquals(100, bookDto.getPage().getTotal());
        assertEquals(10, bookDto.getPage().getSize());


        // read page somewhere inbetween
        response = given()
                .queryParam("page", 6)
                .queryParam("size", 10)
                .when()
                .get("/books")
                .then()
                .statusCode(200)
                .extract()
                .response();

        // check data & page
        bookDto = response.getBody().as(new TypeRef<>() {/**/
        });
        // TODO check if body contains page size
        // check page is 0
        assertEquals(10, bookDto.getBooks().size());
        assertEquals("Book61", bookDto.getBooks().get(0).getTitle());
        assertEquals(6, bookDto.getPage().getNumber());
        assertEquals(100, bookDto.getPage().getTotal());
        assertEquals(10, bookDto.getPage().getSize());
    }

    @Test
    void shouldReadAllPages() {
        // given
        int page = 0;
        BookResourceDto response = given()
                .queryParam("page", page)
                .queryParam("size", 10)
                .when()
                .get("/books")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .getBody()
                .as(new TypeRef<>() {/**/
                });
        assertEquals(0, response.getPage().getNumber());

        // when
        while (response.getPage().isHasNext()) {
            response = given()
                    .queryParam("page", ++page)
                    .queryParam("size", 10)
                    .when()
                    .get("/books")
                    .then()
                    .statusCode(200)
                    .extract()
                    .response()
                    .getBody()
                    .as(new TypeRef<>() {/**/
                    });
        }

        // then
        assertEquals(9, response.getPage().getNumber());
        assertFalse(response.getPage().isHasNext());
        assertEquals("Book100", response.getBooks().get(response.getBooks().size() - 1).getTitle());
    }

    private static void waitForDocumentsBeingIndexed() {
        Awaitility.await().atMost(30, TimeUnit.SECONDS).until(() -> {
                    BookResourceDto bookDto = given()
                            .queryParam("page", 0)
                            .queryParam("size", 1)
                            .when()
                            .get("/books")
                            .then()
                            .statusCode(200)
                            .extract()
                            .response().getBody().as(new TypeRef<>() {
                            });

                    return 100L == bookDto.getPage().getTotal();
                }
        );
    }

    @AfterEach
    void tearDown() {
        bookIds.forEach(id -> given()
                .when()
                .delete("/books/" + id)
                .then()
                .statusCode(204));
    }
}

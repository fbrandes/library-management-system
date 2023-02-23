package com.github.fbrandes.library.openlibrary.service;

import com.github.fbrandes.library.openlibrary.controller.BookDto;
import com.github.fbrandes.library.openlibrary.model.Book;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OpenLibraryServiceIT {
    @LocalServerPort
    private Integer port;

    @Test
    void shouldReturnBooks() {
        // when
        ResponseEntity<BookDto> response = client()
                .exchange(
                        "/books?query={query}&limit={limit}&page={page}",
                        HttpMethod.GET,
                        null,
                        BookDto.class,
                        "the lord of the rings the two towers", 10, 4);

        // then
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());

        List<Book> books = response.getBody().getBooks();
        assertEquals(10, books.size());
    }

    @Test
    void shouldPaginate() {
        BookDto bookDto = client()
                .exchange(
                        "/books?query={query}&limit={limit}&page={page}",
                        HttpMethod.GET,
                        null,
                        BookDto.class,
                        "the lord of the rings the two towers", 10, 1)
                .getBody();
        assertNotNull(bookDto);

        int nextPage = bookDto.getPage() + 1;
        BookDto bookDto2 = client()
                .exchange(
                        "/books?query={query}&limit={limit}&page={page}",
                        HttpMethod.GET,
                        null,
                        BookDto.class,
                        "the lord of the rings the two towers", 10, nextPage)
                .getBody();
        assertNotNull(bookDto2);
        assertEquals(2, bookDto2.getPage());

        // assert that pages are actually different
        assertNotEquals(bookDto.getBooks().get(0).getIsbn(), bookDto2.getBooks().get(0).getIsbn());
    }

    private RestTemplate client() {
        return (new RestTemplateBuilder())
                .rootUri("http://localhost:" + port)
                .build();
    }
}

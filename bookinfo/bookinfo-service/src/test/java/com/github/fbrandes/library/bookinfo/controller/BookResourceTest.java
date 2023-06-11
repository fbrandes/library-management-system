package com.github.fbrandes.library.bookinfo.controller;

import com.github.fbrandes.library.bookinfo.model.Author;
import com.github.fbrandes.library.bookinfo.model.Book;
import com.github.fbrandes.library.bookinfo.model.Isbn;
import com.github.fbrandes.library.bookinfo.service.BookInfoService;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookResourceTest {
    @InjectMocks
    private BookResource bookResource;

    @Mock
    private BookInfoService bookInfoService;

    private List<Book> mockBooks;

    @BeforeEach
    void setup() {
        mockBooks = createMockBooks();
    }

    @Test
    void shouldFindBookById() throws IOException {
        // given
        when(bookInfoService.get(eq("123"))).thenReturn(mockBooks.get(0));

        // when
        Book book = bookResource.find("123");

        /// then
        assertEquals("123", book.getId());
    }

    @Test
    void shouldFindBookByIsbn() throws IOException {
        // given
        when(bookInfoService.searchByIsbn(eq("978-0134685991"))).thenReturn(List.of(mockBooks.get(1)));

        // when
        List<Book> books = bookResource.findByIsbn("978-0134685991");

        // then
        assertEquals(1, books.size());

        Book book = books.get(0);
        assertEquals(1, book.getIsbn().size());
        assertEquals("978-0134685991", book.getIsbn().get(0).getIdentifier());
    }

    @Test
    void shouldThrowOnMissingIsbn() {
        assertThrows(BadRequestException.class, () -> bookResource.findByIsbn(null));
    }

    @Test
    void shouldFindBookByTitle() throws IOException {
        // given
        when(bookInfoService.searchByTitle(eq("The Pragmatic Programmer"))).thenReturn(List.of(mockBooks.get(2)));

        // when
        List<Book> books = bookResource.findByTitle("The Pragmatic Programmer");

        // then
        assertEquals(1, books.size());

        Book book = books.get(0);
        assertEquals(1, book.getIsbn().size());
        assertEquals("The Pragmatic Programmer", book.getTitle());
    }

    @Test
    void shouldThrowOnMissingTitle() {
        assertThrows(BadRequestException.class, () -> bookResource.findByTitle(null));
    }

    @Test
    void shouldFindBookByAuthor() throws IOException {
        // given
        when(bookInfoService.searchByAuthor(eq("Andrew Hunt"))).thenReturn(List.of(mockBooks.get(2)));

        // when
        List<Book> books = bookResource.findByAuthor("Andrew Hunt");

        // then
        assertEquals(1, books.size());

        Book book = books.get(0);
        assertEquals(1, book.getAuthors().stream().filter(a -> a.getName().equals("Andrew Hunt")).toList().size());
    }

    @Test
    void shouldThrowOnMissingAuthor() {
        assertThrows(BadRequestException.class, () -> bookResource.findByAuthor(null));
    }

    @Test
    void shouldReturnSameBookForCoAuthors() throws IOException {
        // given
        when(bookInfoService.searchByAuthor(eq("Andrew Hunt"))).thenReturn(List.of(mockBooks.get(2)));
        when(bookInfoService.searchByAuthor(eq("David Thomas"))).thenReturn(List.of(mockBooks.get(2)));

        // when
        Book book1 = bookResource.findByAuthor("Andrew Hunt").get(0);
        Book book2 = bookResource.findByAuthor("David Thomas").get(0);

        // then
        assertEquals(book1, book2);
    }

    @Test
    void shouldIndexSuccessfully() throws IOException {
        // given
        String id = UUID.randomUUID().toString();
        Book newBook = new Book();
        newBook.setId(id);

        doNothing().when(bookInfoService).index(newBook);

        try (Response response = bookResource.index(newBook)) {
            assertEquals(201, response.getStatus());
            assertEquals("/books/" + id, response.getLocation().toString());
        }
    }

    private List<Book> createMockBooks() {
        Book book1 = new Book();
        book1.setTitle("The Mythical Man-Month");
        book1.setId("123");
        Isbn isbn1 = new Isbn();
        isbn1.setIdentifier("978-0-201-00650-6");
        isbn1.setType(Isbn.Type.ISBN_13);
        Isbn isbn2 = new Isbn();
        isbn2.setIdentifier("978-0-201-83595-3");
        isbn2.setType(Isbn.Type.ISBN_13);
        book1.setIsbn(List.of(isbn1, isbn2));
        book1.setAuthors(List.of(new Author("", "Fred Brooks", Collections.emptyList())));

        Book book2 = new Book();
        book2.setTitle("Effective Java");
        book2.setId("456");
        Isbn isbn3 = new Isbn();
        isbn3.setIdentifier("978-0134685991");
        isbn3.setType(Isbn.Type.ISBN_13);
        book2.setIsbn(List.of(isbn3));
        book2.setAuthors(List.of(new Author("", "Joshua Bloch", Collections.emptyList())));

        Book book3 = new Book();
        book3.setTitle("The Pragmatic Programmer");
        book3.setId("789");
        Isbn isbn4 = new Isbn();
        isbn4.setIdentifier("978-0135957059");
        isbn4.setType(Isbn.Type.ISBN_13);
        book3.setIsbn(List.of(isbn4));
        book3.setAuthors(List.of(new Author("", "Andrew Hunt", Collections.emptyList()),
                new Author("", "David Thomas", Collections.emptyList())));
        return List.of(book1, book2, book3);
    }
}
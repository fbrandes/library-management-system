package com.github.fbrandes.library.bookinfo.controller;

import com.github.fbrandes.library.bookinfo.BookTestDataCreator;
import com.github.fbrandes.library.bookinfo.model.Book;
import com.github.fbrandes.library.bookinfo.service.BookService;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
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
    private BookService bookService;

    private List<Book> mockBooks;

    @BeforeEach
    void setup() {
        mockBooks = BookTestDataCreator.createBooks();
    }

    @Test
    void shouldFindBookById() throws IOException {
        // given
        String findId = mockBooks.get(0).getId();
        when(bookService.get(eq(findId))).thenReturn(mockBooks.get(0));

        // when
        Book book = bookResource.find(findId);

        /// then
        assertEquals(findId, book.getId());
    }

    @Test
    void shouldFindBookByIsbn() throws IOException {
        // given
        when(bookService.searchByIsbn(eq("978-0134685991"))).thenReturn(List.of(mockBooks.get(1)));

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
        when(bookService.searchByTitle(eq("The Pragmatic Programmer"))).thenReturn(List.of(mockBooks.get(2)));

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
        when(bookService.searchByAuthor(eq("Andrew Hunt"))).thenReturn(List.of(mockBooks.get(2)));

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
        when(bookService.searchByAuthor(eq("Andrew Hunt"))).thenReturn(List.of(mockBooks.get(2)));
        when(bookService.searchByAuthor(eq("David Thomas"))).thenReturn(List.of(mockBooks.get(2)));

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

        doNothing().when(bookService).index(newBook);

        try (Response response = bookResource.index(newBook)) {
            assertEquals(201, response.getStatus());
            assertEquals("/books/" + id, response.getLocation().toString());
        }
    }
}
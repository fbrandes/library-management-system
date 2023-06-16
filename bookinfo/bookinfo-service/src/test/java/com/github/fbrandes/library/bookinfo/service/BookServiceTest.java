package com.github.fbrandes.library.bookinfo.service;

import com.github.fbrandes.library.bookinfo.BookTestDataCreator;
import com.github.fbrandes.library.bookinfo.model.Book;
import com.github.fbrandes.library.bookinfo.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    private List<Book> mockBooks;

    @BeforeEach
    void setUp() {
        mockBooks = BookTestDataCreator.createBooks();
    }

    @Test
    void index() throws IOException {
        // given
        doNothing().when(bookRepository).save(mockBooks.get(0));
        Book book = mockBooks.get(0);

        // when
        bookService.index(book);

        // then
        verify(bookRepository).save(book);
    }

    @Test
    void get() throws IOException {
        // given
        String bookId = mockBooks.get(0).getId();
        when(bookRepository.findById(bookId)).thenReturn(mockBooks.get(0));

        // when
        Book book = bookService.get(bookId);

        // then
        verify(bookRepository).findById(bookId);
        assertEquals(bookId, book.getId());
    }

    @Test
    void searchByTitle() throws IOException {
        // given
        String title = mockBooks.get(0).getTitle();
        when(bookRepository.findByTitle(title)).thenReturn(List.of(mockBooks.get(0)));

        // when
        List<Book> books = bookService.searchByTitle(title);

        // then
        verify(bookRepository).findByTitle(title);
        assertEquals(title, books.get(0).getTitle());
    }

    @Test
    void searchByAuthor() throws IOException {
        // given
        String author = mockBooks.get(0).getAuthors().get(0).getName();
        when(bookRepository.findByAuthor(author)).thenReturn(List.of(mockBooks.get(0)));

        // when
        List<Book> books = bookService.searchByAuthor(author);

        // then
        verify(bookRepository).findByAuthor(author);
        assertEquals(author, books.get(0).getAuthors().get(0).getName());
    }

    @Test
    void searchByIsbn() throws IOException {
        // given
        String isbn = mockBooks.get(0).getIsbn().get(0).getIdentifier();
        when(bookRepository.findByIsbn(isbn)).thenReturn(List.of(mockBooks.get(0)));

        // when
        List<Book> books = bookService.searchByIsbn(isbn);

        // then
        verify(bookRepository).findByIsbn(isbn);
        assertEquals(isbn, books.get(0).getIsbn().get(0).getIdentifier());
    }
}
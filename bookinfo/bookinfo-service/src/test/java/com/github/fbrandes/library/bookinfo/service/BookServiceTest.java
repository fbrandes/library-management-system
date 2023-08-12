package com.github.fbrandes.library.bookinfo.service;

import com.github.fbrandes.library.bookinfo.BookTestDataCreator;
import com.github.fbrandes.library.bookinfo.controller.BookResourceDto;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    private BookResourceDto bookResourceDto;

    @BeforeEach
    void setUp() {
        List<Book> books = BookTestDataCreator.createBooks();
        bookResourceDto = BookResourceDto.builder()
                .books(books)
                .page(BookResourceDto.Page.of()
                        .number(0)
                        .total(books.size())
                        .size(books.size())
                        .build())
                .build();
    }

    @Test
    void shouldFindAll() throws IOException {
        // given
        when(bookRepository.findAll(0, 3)).thenReturn(bookResourceDto);

        // when
        BookResourceDto books = bookService.findAll(0, 3);

        // then
        assertFalse(books.getBooks().isEmpty());
        assertEquals(3, books.getBooks().size());
    }

    @Test
    void shouldFindBook() throws IOException {
        // given
        doNothing().when(bookRepository).save(bookResourceDto.getBooks().get(0));
        Book book = bookResourceDto.getBooks().get(0);

        // when
        bookService.save(book);

        // then
        verify(bookRepository).save(book);
    }

    @Test
    void shouldFindById() throws IOException {
        // given
        String bookId = bookResourceDto.getBooks().get(0).getId();
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookResourceDto.getBooks().get(0)));

        // when
        Optional<Book> book = bookService.findById(bookId);
        assertTrue(book.isPresent());

        // then
        verify(bookRepository).findById(bookId);
        assertEquals(bookId, book.get().getId());
    }

    @Test
    void shouldFindByTitle() throws IOException {
        // given
        String title = bookResourceDto.getBooks().get(0).getTitle();
        when(bookRepository.findByTitle(title)).thenReturn(List.of(bookResourceDto.getBooks().get(0)));

        // when
        List<Book> books = bookService.searchByTitle(title);

        // then
        verify(bookRepository).findByTitle(title);
        assertEquals(title, books.get(0).getTitle());
    }

    @Test
    void shouldFindByAuthor() throws IOException {
        // given
        String author = bookResourceDto.getBooks().get(0).getAuthors().get(0).getName();
        when(bookRepository.findByAuthor(author)).thenReturn(List.of(bookResourceDto.getBooks().get(0)));

        // when
        List<Book> books = bookService.searchByAuthor(author);

        // then
        verify(bookRepository).findByAuthor(author);
        assertEquals(author, books.get(0).getAuthors().get(0).getName());
    }

    @Test
    void shouldFindByIsbn() throws IOException {
        // given
        String isbn = bookResourceDto.getBooks().get(0).getIsbn().get(0).getIdentifier();
        when(bookRepository.findByIsbn(isbn)).thenReturn(List.of(bookResourceDto.getBooks().get(0)));

        // when
        List<Book> books = bookService.searchByIsbn(isbn);

        // then
        verify(bookRepository).findByIsbn(isbn);
        assertEquals(isbn, books.get(0).getIsbn().get(0).getIdentifier());
    }

    @Test
    void shouldDeleteBook() {
        // given
        doNothing().when(bookRepository).delete("123");

        // when
        bookService.delete("123");

        // then
        verify(bookRepository).delete("123");
    }
}
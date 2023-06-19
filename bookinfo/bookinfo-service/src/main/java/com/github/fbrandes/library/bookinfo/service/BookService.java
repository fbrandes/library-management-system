package com.github.fbrandes.library.bookinfo.service;

import com.github.fbrandes.library.bookinfo.model.Book;
import com.github.fbrandes.library.bookinfo.repository.BookRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class BookService {
    @Inject
    BookRepository bookRepository;

    public List<Book> findAll(int page, int size) throws IOException {
        return bookRepository.findAll(page, size);
    }

    public void save(Book book) throws IOException {
        bookRepository.save(book);
    }

    public Book update(Book book) throws IOException {
        return bookRepository.update(book);
    }

    public Optional<Book> findById(String id) throws IOException {
        return bookRepository.findById(id);
    }

    public List<Book> searchByTitle(String title) throws IOException {
        return bookRepository.findByTitle(title);
    }

    public List<Book> searchByAuthor(String author) throws IOException {
        return bookRepository.findByAuthor(author);
    }

    public List<Book> searchByIsbn(String isbn) throws IOException {
        return bookRepository.findByIsbn(isbn);
    }

    public void delete(String id) {
        bookRepository.delete(id);
    }
}

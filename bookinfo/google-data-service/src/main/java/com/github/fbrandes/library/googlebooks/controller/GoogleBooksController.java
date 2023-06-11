package com.github.fbrandes.library.googlebooks.controller;

import com.github.fbrandes.library.googlebooks.model.Book;
import com.github.fbrandes.library.googlebooks.service.GoogleBooksService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class GoogleBooksController {
    private GoogleBooksService googleBooksService;

    @GetMapping
    public List<Book> getBooks() {
        return googleBooksService.getBooks("");
    }
}

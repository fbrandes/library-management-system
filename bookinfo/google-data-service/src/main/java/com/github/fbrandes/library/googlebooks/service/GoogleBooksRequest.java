package com.github.fbrandes.library.googlebooks.service;

import com.github.fbrandes.library.googlebooks.model.Book;
import lombok.Data;

import java.util.List;

@Data
public class GoogleBooksRequest {
    private String kind;
    private long totalItems;
    private List<Book> items;
}

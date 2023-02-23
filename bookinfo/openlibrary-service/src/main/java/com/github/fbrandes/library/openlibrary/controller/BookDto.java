package com.github.fbrandes.library.openlibrary.controller;

import com.github.fbrandes.library.openlibrary.model.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private List<Book> books;

    private int total;

    private int totalPages;

    private int page;

    private int offset;
}

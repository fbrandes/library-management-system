package com.github.fbrandes.library.bookinfo.controller;

import com.github.fbrandes.library.bookinfo.model.Book;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BookResourceDto {
    private final List<Book> books;
    private final Page page;

    @Data
    @Builder(builderMethodName = "of")
    public static class Page {
        int number;
        long total;
        int size;
        boolean hasNext;
    }
}

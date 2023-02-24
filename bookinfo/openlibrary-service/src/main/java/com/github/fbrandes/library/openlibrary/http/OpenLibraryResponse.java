package com.github.fbrandes.library.openlibrary.http;

import com.github.fbrandes.library.openlibrary.model.Book;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class OpenLibraryResponse {
    private int start;
    private int numFound;
    @SerializedName("docs")
    private List<Book> documents;
}

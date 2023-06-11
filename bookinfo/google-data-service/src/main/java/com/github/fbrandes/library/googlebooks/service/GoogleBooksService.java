package com.github.fbrandes.library.googlebooks.service;

import com.github.fbrandes.library.googlebooks.model.Book;
import com.github.fbrandes.library.googlebooks.model.Isbn;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleBooksService {
    private static final String API_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleBooksService.class);

    private final HttpClient httpClient;

    public GoogleBooksService() {
        this.httpClient = HttpClient.newBuilder().build();
    }

    public List<Book> getBooks(String query) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(API_URL + URLEncoder.encode(query, StandardCharsets.UTF_8))).build();
        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            LOGGER.error(e.getMessage());
        }

        if (response != null && response.body() != null) {
            return fromJson(response.body());
        }

        return Collections.emptyList();
    }

    private List<Book> fromJson(String json) {
        return new Gson().fromJson(json, GoogleBooksRequest.class).getItems();
    }
}
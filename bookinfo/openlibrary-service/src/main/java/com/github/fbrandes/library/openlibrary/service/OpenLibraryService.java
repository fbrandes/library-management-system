package com.github.fbrandes.library.openlibrary.service;

import com.github.fbrandes.library.openlibrary.controller.BookDto;
import com.github.fbrandes.library.openlibrary.http.OpenLibraryResponse;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;

@Slf4j
@Service
public class OpenLibraryService {
    private static final String API_URL = "https://openlibrary.org/search.json?q=%s&page=%s&limit=%s";

    private final HttpClient httpClient;

    public OpenLibraryService(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public BookDto getBooks(final String query, int page, int limit) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(String.format(API_URL, query, page, limit))).build();
        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
        }

        if (response != null && response.body() != null) {
            return toDto(response, page, limit);
        }

        return BookDto.builder().books(Collections.emptyList()).total(0).page(0).build();
    }

    private BookDto toDto(HttpResponse<String> response, int page, int limit) {
        OpenLibraryResponse request = new Gson().fromJson(response.body(), OpenLibraryResponse.class);
        return BookDto.builder()
                .books(request.getDocuments())
                .total(request.getNumFound())
                .page(page)
                .totalPages(request.getNumFound() / limit)
                .offset(request.getStart())
                .build();
    }
}

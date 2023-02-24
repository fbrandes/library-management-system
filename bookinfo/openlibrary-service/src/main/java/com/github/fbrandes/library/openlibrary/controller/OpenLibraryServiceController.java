package com.github.fbrandes.library.openlibrary.controller;

import com.github.fbrandes.library.openlibrary.service.OpenLibraryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@AllArgsConstructor
public class OpenLibraryServiceController {
    private OpenLibraryService openLibraryService;

    @GetMapping(value = "books", params = {"page", "limit"})
    public ResponseEntity<BookDto> find(@RequestParam String query, @RequestParam int page, @RequestParam int limit) {
        return ResponseEntity.ok(openLibraryService.getBooks(URLEncoder.encode(query, StandardCharsets.UTF_8), page, limit));
    }
}

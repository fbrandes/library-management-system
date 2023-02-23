package com.github.fbrandes.library.openlibrary.service;

import com.github.fbrandes.library.openlibrary.controller.BookDto;
import com.github.fbrandes.library.openlibrary.http.OpenLibraryResponse;
import com.github.fbrandes.library.openlibrary.model.Book;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(OutputCaptureExtension.class)
class OpenLibraryServiceTest {
    @InjectMocks
    private OpenLibraryService openLibraryService;

    @Mock
    private HttpClient httpClient;

    @Mock
    private HttpResponse<String> httpResponse;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnBooks() throws IOException, InterruptedException {
        // given
        when(httpClient.send(any(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(httpResponse);

        OpenLibraryResponse request = new OpenLibraryResponse();
        request.setDocuments(List.of(new Book(), new Book()));
        when(httpResponse.body()).thenReturn(new Gson().toJson(request));

        // when
        BookDto bookDto = openLibraryService.getBooks("lord", 0, 10);

        // then
        assertEquals(2, bookDto.getBooks().size());
    }

    @ParameterizedTest
    @MethodSource("exceptionsThrownOnError")
    void shouldNotCrashOnFetchError(Exception exception, String reason) throws IOException, InterruptedException {
        when(httpClient.send(any(), any())).thenThrow(exception);
        BookDto bookDto = assertDoesNotThrow(() -> openLibraryService.getBooks("something", 0, 10));
        assertNotNull(bookDto);
        assertTrue(bookDto.getBooks().isEmpty());
    }

    private static Stream<Arguments> exceptionsThrownOnError() {
        return Stream.of(
                Arguments.of(new IOException(), "Error while fetching data"),
                Arguments.of(new InterruptedException(), "Thread interrupted while fetching data")
        );
    }
}
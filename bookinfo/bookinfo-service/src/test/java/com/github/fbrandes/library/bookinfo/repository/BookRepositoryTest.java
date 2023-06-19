package com.github.fbrandes.library.bookinfo.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import com.github.fbrandes.library.bookinfo.BookTestDataCreator;
import com.github.fbrandes.library.bookinfo.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookRepositoryTest {

    @InjectMocks
    private BookRepository bookRepository;

    @Mock
    private ElasticsearchClient elasticsearchClient;

    @Mock
    private GetResponse<Book> getResponse;

    @Mock
    private SearchResponse<Book> searchResponse;

    @Mock
    private DeleteResponse deleteResponse;

    @Mock
    private HitsMetadata<Book> hitsMetadata;

    @Mock
    private Hit<Book> hit;

    private List<Book> mockBooks;

    @BeforeEach
    void setup() {
        mockBooks = BookTestDataCreator.createBooks();
    }

    @Test
    void shouldSaveBookSuccessfully() throws IOException {
        // given
        when(elasticsearchClient.index(ArgumentMatchers.<IndexRequest<Book>>any())).thenReturn(null);
        Book book = mockBooks.get(0);

        // when
        bookRepository.save(book);

        // then
        verify(elasticsearchClient).index(ArgumentMatchers.<IndexRequest<Book>>any());
    }

    @Test
    void shouldFindBookById() throws IOException {
        // given
        String id = mockBooks.get(0).getId();
        when(elasticsearchClient.get(ArgumentMatchers.<GetRequest>any(), eq(Book.class))).thenReturn(getResponse);
        when(getResponse.found()).thenReturn(true);
        when(getResponse.source()).thenReturn(mockBooks.get(0));

        // when
        Optional<Book> book = bookRepository.findById(id);
        assertTrue(book.isPresent());

        // then
        verify(elasticsearchClient).get(ArgumentMatchers.<GetRequest>any(), eq(Book.class));
        assertEquals(id, book.get().getId());
    }

    @Test
    void shouldFindBookByTitle() throws IOException {
        // given
        String title = mockBooks.get(0).getTitle();
        when(elasticsearchClient.search(ArgumentMatchers.<SearchRequest>any(), eq(Book.class))).thenReturn(searchResponse);
        when(searchResponse.hits()).thenReturn(hitsMetadata);
        when(hitsMetadata.hits()).thenReturn(List.of(hit));
        when(hit.source()).thenReturn(mockBooks.get(0));

        // when
        List<Book> books = bookRepository.findByTitle(title);

        // then
        verify(elasticsearchClient).search(ArgumentMatchers.<SearchRequest>any(), eq(Book.class));
        verify(hitsMetadata).hits();
        verify(hit).source();
        assertEquals(title, books.get(0).getTitle());
    }

    @Test
    void shouldFindBookByAuthor() throws IOException {
        // given
        String author = mockBooks.get(0).getAuthors().get(0).getName();
        when(elasticsearchClient.search(ArgumentMatchers.<SearchRequest>any(), eq(Book.class))).thenReturn(searchResponse);
        when(searchResponse.hits()).thenReturn(hitsMetadata);
        when(hitsMetadata.hits()).thenReturn(List.of(hit));
        when(hit.source()).thenReturn(mockBooks.get(0));

        // when
        List<Book> books = bookRepository.findByAuthor(author);

        // then
        verify(elasticsearchClient).search(ArgumentMatchers.<SearchRequest>any(), eq(Book.class));
        verify(hitsMetadata).hits();
        verify(hit).source();
        assertEquals(author, books.get(0).getAuthors().get(0).getName());
    }

    @Test
    void shouldFindBookByIsbn() throws IOException {
        // given
        String isbn = mockBooks.get(0).getIsbn().get(0).getIdentifier();
        when(elasticsearchClient.search(ArgumentMatchers.<SearchRequest>any(), eq(Book.class))).thenReturn(searchResponse);
        when(searchResponse.hits()).thenReturn(hitsMetadata);
        when(hitsMetadata.hits()).thenReturn(List.of(hit));
        when(hit.source()).thenReturn(mockBooks.get(0));

        // when
        List<Book> books = bookRepository.findByIsbn(isbn);

        // then
        verify(elasticsearchClient).search(ArgumentMatchers.<SearchRequest>any(), eq(Book.class));
        verify(hitsMetadata).hits();
        verify(hit).source();
        assertEquals(isbn, books.get(0).getIsbn().get(0).getIdentifier());
    }

    @Test
    void shouldDeleteBookSuccessfully() throws IOException {
        // given
        when(elasticsearchClient.delete(any(DeleteRequest.class))).thenReturn(deleteResponse);

        // when
        bookRepository.delete("123");

        // then
        verify(elasticsearchClient).delete(any(DeleteRequest.class));
    }
}
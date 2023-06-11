package com.github.fbrandes.library.bookinfo.repository;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import com.github.fbrandes.library.bookinfo.model.Book;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class BookRepository {
    private static final String BOOK_INDEX = "books";
    @Inject
    ElasticsearchClient client;

    public void save(Book book) throws IOException {
        String id = Optional.of(book.getId()).orElse(UUID.randomUUID().toString());
        IndexRequest<Book> request = IndexRequest.of(
                b -> b.index(BOOK_INDEX)
                        .id(id)
                        .document(book));
        client.index(request);
    }

    public Book findById(String id) throws IOException {
        GetRequest getRequest = GetRequest.of(
                b -> b.index(BOOK_INDEX)
                        .id(id));
        GetResponse<Book> getResponse = client.get(getRequest, Book.class);
        if (getResponse.found()) {
            return getResponse.source();
        }
        return null;
    }

    public List<Book> findByTitle(String title) throws IOException {
        return find("title", title);
    }

    public List<Book> findByAuthor(String author) throws IOException {
        return find("author", author);
    }
    public List<Book> findByIsbn(String isbn) throws IOException {
        return find("isbn", isbn);
    }

    private List<Book> find(String term, String match) throws IOException {
        SearchRequest searchRequest = SearchRequest.of(
                b -> b.index(BOOK_INDEX)
                        .query(QueryBuilders.match().field(term).query(FieldValue.of(match)).build()._toQuery()));

        SearchResponse<Book> searchResponse = client.search(searchRequest, Book.class);
        HitsMetadata<Book> hits = searchResponse.hits();
        return hits.hits().stream().map(Hit::source).toList();
    }
}

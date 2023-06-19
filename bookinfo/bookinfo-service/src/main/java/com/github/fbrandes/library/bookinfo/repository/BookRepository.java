package com.github.fbrandes.library.bookinfo.repository;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.InlineGet;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import com.github.fbrandes.library.bookinfo.model.Book;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.InternalServerErrorException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class BookRepository {
    private static final String BOOK_INDEX = "books";
    @Inject
    ElasticsearchClient client;

    public List<Book> findAll(int page, int size) throws IOException {
        SearchRequest sr = SearchRequest.of(s -> s
            .index(BOOK_INDEX)
            .query(QueryBuilders.matchAll().build()._toQuery())
            .size(size)
            .from(page));

        SearchResponse<Book> searchResponse = client.search(sr, Book.class);
        HitsMetadata<Book> hits = searchResponse.hits();
        return hits.hits().stream().map(Hit::source).toList();
    }

    public void save(Book book) throws IOException {
        String id = Optional.of(book.getId()).orElse(UUID.randomUUID().toString());
        IndexRequest<Book> request = IndexRequest.of(
                b -> b.index(BOOK_INDEX)
                        .id(id)
                        .document(book));
        client.index(request);
    }

    public Book update(Book book) throws IOException {
        UpdateRequest<Book, Book> request = UpdateRequest.of(u -> u.index(BOOK_INDEX).doc(book));

        UpdateResponse<Book> updateResponse = client.update(request, Book.class);

        InlineGet<Book> result = updateResponse.get();
        if(result != null && result.found()) {
            if(result.source() == null) {
                throw new InternalServerErrorException("update failed");
            }

            return result.source();
        }

        throw new InternalServerErrorException("update failed");
    }

    public Optional<Book> findById(String id) throws IOException {
        GetRequest getRequest = GetRequest.of(
                b -> b.index(BOOK_INDEX)
                        .id(id));
        GetResponse<Book> getResponse = client.get(getRequest, Book.class);
        if (getResponse.found()) {
            if(getResponse.source() == null) {
                return Optional.empty();
            }
            return Optional.of(getResponse.source());
        }
        return Optional.empty();
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

    public void delete(String id) {
        try {
            client.delete(DeleteRequest.of(d -> d.index(BOOK_INDEX).id(id)));
        } catch (IOException e) {
            throw new InternalServerErrorException(e);
        }
    }
}

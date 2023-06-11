package com.github.fbrandes.library.bookinfo.controller;

import com.github.fbrandes.library.bookinfo.model.Book;
import com.github.fbrandes.library.bookinfo.service.BookInfoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jboss.resteasy.reactive.RestQuery;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Path("/books")
public class BookResource {
    @Inject
    BookInfoService bookInfoService;

    @Operation(operationId = "find", description = "Find book with matching id")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Successful retrieval"),
            @APIResponse(responseCode = "400", description = "invalid query"),
            @APIResponse(responseCode = "401", description = "Request not authorized"),
            @APIResponse(responseCode = "404", description = "Service not found"),
            @APIResponse(responseCode = "500", description = "Server error")
    })
    @GET
    @Path("/{id}")
    public Book find(String id) throws IOException {
        return bookInfoService.get(id);
    }

    @Operation(operationId = "findByIsbn", description = "Find books with matching ISBN")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Successful retrieval"),
            @APIResponse(responseCode = "400", description = "invalid query"),
            @APIResponse(responseCode = "401", description = "Request not authorized"),
            @APIResponse(responseCode = "404", description = "Service not found"),
            @APIResponse(responseCode = "500", description = "Server error")
    })
    @GET
    @Path("/isbn")
    public List<Book> findByIsbn(@RestQuery String isbn) throws IOException {
        if (isbn != null) {
            return bookInfoService.searchByIsbn(isbn);
        } else {
            throw new BadRequestException("Should provide isbn query parameter");
        }
    }

    @Operation(operationId = "findByTitle", description = "Find books with matching title")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Successful retrieval"),
            @APIResponse(responseCode = "400", description = "invalid query"),
            @APIResponse(responseCode = "401", description = "Request not authorized"),
            @APIResponse(responseCode = "404", description = "Service not found"),
            @APIResponse(responseCode = "500", description = "Server error")
    })
    @GET
    @Path("/title")
    public List<Book> findByTitle(@RestQuery String title) throws IOException {
        if (title != null) {
            return bookInfoService.searchByTitle(title);
        } else {
            throw new BadRequestException("Should provide title query parameter");
        }
    }

    @Operation(operationId = "findByAuthor", description = "Find books with matching title")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Successful retrieval"),
            @APIResponse(responseCode = "400", description = "invalid query"),
            @APIResponse(responseCode = "401", description = "Request not authorized"),
            @APIResponse(responseCode = "404", description = "Service not found"),
            @APIResponse(responseCode = "500", description = "Server error")
    })
    @GET
    @Path("/author")
    public List<Book> findByAuthor(@RestQuery String author) throws IOException {
        if (author != null) {
            return bookInfoService.searchByAuthor(author);
        } else {
            throw new BadRequestException("Should provide author query parameter");
        }
    }

    @Operation(operationId = "indexBooks", description = "Index new book data")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Data successful indexed"),
            @APIResponse(responseCode = "400", description = "invalid data"),
            @APIResponse(responseCode = "401", description = "Request not authorized"),
            @APIResponse(responseCode = "404", description = "Service not found"),
            @APIResponse(responseCode = "500", description = "Server error")
    })
    @POST
    public Response index(Book book) throws IOException {
        if (book.getId() == null) {
            book.setId(UUID.randomUUID().toString());
        }
        bookInfoService.index(book);
        return Response.created(URI.create("/books/" + book.getId())).build();
    }
}

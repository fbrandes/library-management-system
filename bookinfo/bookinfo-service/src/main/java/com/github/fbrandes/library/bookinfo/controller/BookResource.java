package com.github.fbrandes.library.bookinfo.controller;

import com.github.fbrandes.library.bookinfo.model.Book;
import com.github.fbrandes.library.bookinfo.service.BookService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jboss.resteasy.reactive.RestQuery;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Path("/books")
public class BookResource {
    @Inject
    BookService bookService;

    @Operation(operationId = "find", description = "Find book with matching id")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Successful retrieval"),
        @APIResponse(responseCode = "400", description = "invalid query"),
        @APIResponse(responseCode = "401", description = "Request not authorized"),
        @APIResponse(responseCode = "404", description = "Service not found"),
        @APIResponse(responseCode = "500", description = "Server error")
    })
    @GET
    public Response findAll(@RestQuery int page, @RestQuery int size) throws IOException {
        List<Book> books = bookService.findAll(page, size);
        return Response.ok().entity(books).build();
    }

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
    public Response findById(String id) throws IOException {
        Optional<Book> book = bookService.findById(id);
        if(book.isPresent()) {
            return Response.ok().entity(book.get()).build();
        } else {
            throw new NotFoundException("No Book found with id " + id);
        }
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
    public Response findByIsbn(@RestQuery String isbn) throws IOException {
        if (isbn != null) {
            return Response.ok().entity(bookService.searchByIsbn(isbn)).build();
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
    public Response findByTitle(@RestQuery String title) throws IOException {
        if (title != null) {
            return Response.ok().entity(bookService.searchByTitle(title)).build();
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
    public Response findByAuthor(@RestQuery String author) throws IOException {
        if (author != null) {
            return Response.ok().entity(bookService.searchByAuthor(author)).build();
        } else {
            throw new BadRequestException("Should provide author query parameter");
        }
    }

    @Operation(operationId = "createBook", description = "Create new book data")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Data successful indexed"),
            @APIResponse(responseCode = "400", description = "invalid data"),
            @APIResponse(responseCode = "401", description = "Request not authorized"),
            @APIResponse(responseCode = "404", description = "Service not found"),
            @APIResponse(responseCode = "500", description = "Server error")
    })
    @POST
    public Response create(Book book) throws IOException {
        if (book.getId() == null) {
            book.setId(UUID.randomUUID().toString());
        }
        bookService.save(book);
        return Response.created(URI.create("/books/" + book.getId())).build();
    }

    @Operation(operationId = "updateBook", description = "Update book data")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Data successful indexed"),
        @APIResponse(responseCode = "400", description = "invalid data"),
        @APIResponse(responseCode = "401", description = "Request not authorized"),
        @APIResponse(responseCode = "404", description = "Service not found"),
        @APIResponse(responseCode = "500", description = "Server error")
    })
    @POST
    public Response update(Book book) throws IOException {
        Book updatedBook = bookService.update(book);
        return Response.ok().entity(updatedBook).build();
    }

    @Operation(operationId = "delete", description = "Find book with matching id")
    @APIResponses(value = {
        @APIResponse(responseCode = "200", description = "Successful retrieval"),
        @APIResponse(responseCode = "400", description = "invalid query"),
        @APIResponse(responseCode = "401", description = "Request not authorized"),
        @APIResponse(responseCode = "404", description = "Service not found"),
        @APIResponse(responseCode = "500", description = "Server error")
    })
    @DELETE
    @Path("/{id}")
    public Response delete(String id) throws IOException {
        try {
            bookService.delete(id);
        } catch (Exception e) {
            // TODO needs better error handling (more detailed errors)
            throw new InternalServerErrorException(e);
        }
        return Response.noContent().build();
    }
}

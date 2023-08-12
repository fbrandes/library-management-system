package com.github.fbrandes.library.bookinfo;

import com.github.fbrandes.library.bookinfo.model.Author;
import com.github.fbrandes.library.bookinfo.model.Book;
import com.github.fbrandes.library.bookinfo.model.Isbn;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class BookTestDataCreator {

    public static List<Book> createBooks() {
        Book book1 = new Book();
        book1.setTitle("The Mythical Man-Month");
        book1.setId(UUID.randomUUID().toString());
        book1.setIsbn(List.of(new Isbn(Isbn.Type.ISBN_13, "978-0-201-00650-6"),
                new Isbn(Isbn.Type.ISBN_13,"978-0-201-83595-3")));
        book1.setAuthors(List.of(new Author("", "Fred Brooks", Collections.emptyList())));

        Book book2 = new Book();
        book2.setTitle("Effective Java");
        book2.setId(UUID.randomUUID().toString());
        book2.setIsbn(List.of(new Isbn(Isbn.Type.ISBN_13,"978-0134685991")));
        book2.setAuthors(List.of(new Author("", "Joshua Bloch", Collections.emptyList())));

        Book book3 = new Book();
        book3.setTitle("The Pragmatic Programmer");
        book3.setId(UUID.randomUUID().toString());
        book3.setIsbn(List.of(new Isbn(Isbn.Type.ISBN_13,"978-0135957059")));
        book3.setAuthors(List.of(new Author("", "Andrew Hunt", Collections.emptyList()),
                new Author("", "David Thomas", Collections.emptyList())));
        return List.of(book1, book2, book3);
    }
}

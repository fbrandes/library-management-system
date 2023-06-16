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
        Isbn isbn1 = new Isbn();
        isbn1.setIdentifier("978-0-201-00650-6");
        isbn1.setType(Isbn.Type.ISBN_13);
        Isbn isbn2 = new Isbn();
        isbn2.setIdentifier("978-0-201-83595-3");
        isbn2.setType(Isbn.Type.ISBN_13);
        book1.setIsbn(List.of(isbn1, isbn2));
        book1.setAuthors(List.of(new Author("", "Fred Brooks", Collections.emptyList())));

        Book book2 = new Book();
        book2.setTitle("Effective Java");
        book2.setId(UUID.randomUUID().toString());
        Isbn isbn3 = new Isbn();
        isbn3.setIdentifier("978-0134685991");
        isbn3.setType(Isbn.Type.ISBN_13);
        book2.setIsbn(List.of(isbn3));
        book2.setAuthors(List.of(new Author("", "Joshua Bloch", Collections.emptyList())));

        Book book3 = new Book();
        book3.setTitle("The Pragmatic Programmer");
        book3.setId(UUID.randomUUID().toString());
        Isbn isbn4 = new Isbn();
        isbn4.setIdentifier("978-0135957059");
        isbn4.setType(Isbn.Type.ISBN_13);
        book3.setIsbn(List.of(isbn4));
        book3.setAuthors(List.of(new Author("", "Andrew Hunt", Collections.emptyList()),
                new Author("", "David Thomas", Collections.emptyList())));
        return List.of(book1, book2, book3);
    }
}

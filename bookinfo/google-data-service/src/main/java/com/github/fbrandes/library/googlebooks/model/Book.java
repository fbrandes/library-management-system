package com.github.fbrandes.library.googlebooks.model;

import lombok.Data;

import java.net.URL;

@Data
public class Book {
    private String kind;
    private String id;
    private String etag;
    private URL selfLink;
    private VolumeInfo volumeInfo;
    private SaleInfo saleInfo;
    private AccessInfo accessInfo;
    private SearchInfo searchInfo;

    public Book() {}
}

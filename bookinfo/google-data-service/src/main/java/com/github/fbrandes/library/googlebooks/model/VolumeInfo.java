package com.github.fbrandes.library.googlebooks.model;

import lombok.Data;

import java.net.URL;
import java.util.List;

@Data
public class VolumeInfo {
    private String title;
    private List<String> authors;
    private String publisher;
    private String publishedDate;
    private String description;
    private List<Isbn> industryIdentifiers;
    private ReadingMode readingMode;
    private int pageCount;
    private String printType;
    private List<String> categories;
    private String maturityRating;
    private boolean allowAnonLogging;
    private String contentVersion;
    private String language;
    private URL previewLink;
    private URL infoLink;
    private URL canonicalVolumeLink;
    private URL webReaderLink;
}

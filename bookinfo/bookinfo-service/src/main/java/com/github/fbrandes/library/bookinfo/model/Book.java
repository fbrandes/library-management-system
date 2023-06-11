package com.github.fbrandes.library.bookinfo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Locale;

@Data
//@Document(indexName = "books", type = "book")
//@ApiModel(value = "book", discriminator = "type")
@NoArgsConstructor
public class Book {
    private String id;

//    @ApiModelProperty(required = true)
    private List<Isbn> isbn;
//    @ApiModelProperty(required = true)
    private String title;

    private List<String> alternativeTitles;
    private List<Author> authors;

    private String publisher;
    private String publishedYear;
    private String firstPublished;
    private String description;
    private Locale language;
    private Metadata metadata;
    private SaleInfo saleInfo;
    private AccessInfo accessInfo;
    private List<String> categories;
    private MaturityRating maturityRating;
    private List<String> editions;
    private PrintType printType;
    private Tags tags;
    private SearchInfo searchInfo;

    private int pageCount;
}
package com.github.fbrandes.library.openlibrary.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class Book {
    private List<String> isbn;
    private String pagination;
    private List<Identifier> identifiers;
    private TableOfContents toc;
    private List<Link> links;
    private Weight weight;
    private String title;
    @SerializedName("title_suggest")
    private String titleSuggest;
    private URL url;
    private Map<String, String> classifications; // key, value
    private String notes;
    private Integer numberOfPages;
    private Cover cover;
    private List<Subject> subjects;
    @SerializedName("publish_date")
    private List<String> publishDate;
    @SerializedName("key")
    private String openLibraryKey;
    private String by;
    private List<PublishPlace> publishPlaces;
    @SerializedName("edition_key")
    private List<String> editionKeys;
    @SerializedName("has_fulltext")
    private boolean hasFullText;
    private List<String> text;
    @SerializedName("author_name")
    private List<String> authorName;
    @SerializedName("seed")
    private List<String> seed;
    @SerializedName("oclc")
    private List<String> oclc;
    @SerializedName("author_key")
    private List<String> authorKey;
    private Availability availability;
    private String type;
    @SerializedName("ebook_count_i")
    private String ebookCountI;
    @SerializedName("edition_count")
    private String editionCount;
    @SerializedName("id_goodreads")
    private List<String> goodReadIds;
    private List<String> publisher; // "publisher": "John Doe"
    private List<String> language;
    @SerializedName("last_modified_i")
    private String lastModifiedI;
    @SerializedName("id_librarything")
    private List<String> idLibraryThing;
    @SerializedName("publish_year")
    private List<String> publishYear;
    @SerializedName("first_publish_year")
    private String firstPublishYear;
}

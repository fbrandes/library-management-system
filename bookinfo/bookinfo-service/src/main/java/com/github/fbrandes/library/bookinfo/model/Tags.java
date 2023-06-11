package com.github.fbrandes.library.bookinfo.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class Tags {
    @SerializedName("gbooks_id") private String googleBooksId;
    @SerializedName("gbooks_etag") private String googleBooksEtag;
    @SerializedName("openlib_key") private String openLibraryKey;
    @SerializedName("id_goodreads") private List<String> idGoodreads;
    @SerializedName("id_libraryanything") private List<String> idLibraryAnything;
    @SerializedName("openlib_last_modified_i") private long openLibraryLastModifiedI;
    private List<String> oclc;
}

package com.github.fbrandes.library.bookinfo.model;

import lombok.Data;

@Data
public class DocumentType {
    private boolean available;
}

//public enum DocumentType {
//    EPUB(false, null), PDF(false, null);
//
//    private boolean availability;
//    private URL acsTokenLink;
//
//    DocumentType(boolean availability, URL acsTokenLink) {
//        this.availability = availability;
//        this.acsTokenLink = acsTokenLink;
//    }
//}

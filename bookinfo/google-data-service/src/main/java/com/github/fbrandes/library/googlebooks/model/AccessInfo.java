package com.github.fbrandes.library.googlebooks.model;

import lombok.Data;

import java.net.URL;

@Data
public class AccessInfo {
    private String country;
    private String viewability;
    private boolean embeddable;
    private boolean publicDomain;
    private String textToSpeechPermission;
    private DocumentType epub;
    private DocumentType pdf;
    private URL webReaderLink;
    private String accessViewStatus;
    private boolean quoteSharinAllowed;
}

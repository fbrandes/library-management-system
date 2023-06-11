package com.github.fbrandes.library.bookinfo.model;

import lombok.Data;

import java.net.URL;

@Data
public class Metadata {
    private URL selfLink;
    private URL previewLink;
    private URL infoLink;
    private URL canonicalVolumeLink;
    private URL webReaderLink;
}

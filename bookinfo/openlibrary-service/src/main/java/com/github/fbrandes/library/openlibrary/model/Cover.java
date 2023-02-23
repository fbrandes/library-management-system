package com.github.fbrandes.library.openlibrary.model;

import lombok.Data;

import java.net.URL;

@Data
public class Cover {
    URL small, medium, large, extraLarge, smallThumbnail, thumbnail;
}

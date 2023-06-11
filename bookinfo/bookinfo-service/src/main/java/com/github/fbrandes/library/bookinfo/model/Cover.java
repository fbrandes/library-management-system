package com.github.fbrandes.library.bookinfo.model;

import lombok.Data;

import java.net.URL;

@Data
public class Cover {
    URL small, medium, large, extraLarge, smallThumbnail, thumbnail;
}

package com.github.fbrandes.library.openlibrary.model;

import java.util.List;

public class TableOfContents {
    private List<ContentItem> contents;

    private class ContentItem {
        String title;
        String label;
        String pageNum;
        Integer level;
    }
}

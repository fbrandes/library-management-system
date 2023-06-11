package com.github.fbrandes.library.bookinfo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Author {
    private String openLibAuthoryKey;
    private String name;
    private List<String> aliases;
}

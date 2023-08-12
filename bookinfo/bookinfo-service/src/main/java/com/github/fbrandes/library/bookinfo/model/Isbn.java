package com.github.fbrandes.library.bookinfo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Isbn {
    private Type type;
    private String identifier;
    public enum Type {
        ISBN_10, ISBN_13;
    }
}

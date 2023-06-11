package com.github.fbrandes.library.googlebooks.model;

import lombok.Data;

@Data
public class Isbn {
    private Type type;
    private String identifier;
    public enum Type {
        ISBN_10, ISBN_13;
    }
}

package com.github.fbrandes.library.openlibrary.model;

import lombok.Data;

@Data
public class Identifier {
    String name;
    String id; // is child of name
}

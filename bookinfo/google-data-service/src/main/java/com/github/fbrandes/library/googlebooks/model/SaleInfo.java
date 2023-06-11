package com.github.fbrandes.library.googlebooks.model;

import lombok.Data;

@Data
public class SaleInfo {
    private String country;
    private Saleability saleability;
    private boolean eBook;

    enum Saleability {
        FOR_SALE;
    }
}

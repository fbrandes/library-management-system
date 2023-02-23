package com.github.fbrandes.library.openlibrary.model;

import java.math.BigDecimal;

public class Weight {
    private BigDecimal weight;
    private WeightUnit unit;

    enum WeightUnit {
        GRAM, KILOGRAM
    }
}

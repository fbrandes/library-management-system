package com.github.fbrandes.library.googlebooks.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Price {
    private BigDecimal amount;
    private String currencyCode;
}

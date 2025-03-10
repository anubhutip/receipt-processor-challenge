package com.receiptprocessor.challenge.model;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;

public class ReceiptItem {

    @NotNull
    @Pattern(regexp = "^[\\w\\s\\-]+$")
    private String shortDescription;

    @NotNull
    @Pattern(regexp = "^\\d+\\.\\d{2}$")
    private String price;

    // Getters and Setters
    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

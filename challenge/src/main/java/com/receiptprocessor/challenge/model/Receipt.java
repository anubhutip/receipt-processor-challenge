package com.receiptprocessor.challenge.model;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public class Receipt {
    @NotNull
    @Pattern(regexp = "^[\\w\\s\\-&]+$")
    private String retailer;

    @NotNull
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$")
    private String purchaseDate;

    @NotNull
    @Pattern(regexp = "^\\d{2}:\\d{2}$")
    private String purchaseTime;

    @NotNull
    private List<ReceiptItem> items;

    @NotNull
    @Pattern(regexp = "^\\d+\\.\\d{2}$")
    private String total;

    // Getters and Setters
    public String getRetailer() {
        return retailer;
    }

    public void setRetailer(String retailer) {
        this.retailer = retailer;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(String purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public List<ReceiptItem> getItems() {
        return items;
    }

    public void setItems(List<ReceiptItem> items) {
        this.items = items;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}

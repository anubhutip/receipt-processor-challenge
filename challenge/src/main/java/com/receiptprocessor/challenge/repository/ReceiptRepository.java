package com.receiptprocessor.challenge.repository;

import com.receiptprocessor.challenge.model.Receipt;

public interface ReceiptRepository {
    void saveReceipt(String receiptId, Receipt receipt);
    Receipt findReceiptById(String receiptId);
}

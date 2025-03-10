package com.receiptprocessor.challenge.repository;

import com.receiptprocessor.challenge.model.Receipt;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ReceiptRepositoryImpl implements ReceiptRepository {
    private final Map<String, Receipt> receiptStorage = new HashMap<>();

    // Saves receiptId and Receipt in map.
    @Override
    public void saveReceipt(String receiptId, Receipt receipt) {
        receiptStorage.put(receiptId, receipt);
    }

    // Returns Receipt from map for given receiptId.
    @Override
    public Receipt findReceiptById(String receiptId) {
        return receiptStorage.get(receiptId);
    }
}

package com.receiptprocessor.challenge;

import com.receiptprocessor.challenge.model.Receipt;
import com.receiptprocessor.challenge.repository.ReceiptRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ReceiptRepositoryImplTest {

    private ReceiptRepositoryImpl receiptRepository;

    @BeforeEach
    void setUp() {
        receiptRepository = new ReceiptRepositoryImpl();
    }

    //Checks if the receipt is saved correctly and then retried from the map.
    @Test
    void testSaveAndFindReceiptPositive() {
        Receipt receipt = new Receipt();
        String receiptId = UUID.randomUUID().toString();

        receiptRepository.saveReceipt(receiptId, receipt);
        Receipt retrieved = receiptRepository.findReceiptById(receiptId);

        assertNotNull(retrieved);
        assertEquals(receipt, retrieved);
    }

    //Checks for a receipt id which does not exist, it should return null.
    @Test
    void testFindReceiptById_InvalidId_ShouldReturnNull() {
        Receipt receipt = receiptRepository.findReceiptById("non-existent-id");
        assertNull(receipt);
    }
}

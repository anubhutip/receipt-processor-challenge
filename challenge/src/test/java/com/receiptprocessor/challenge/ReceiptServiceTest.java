package com.receiptprocessor.challenge;


import com.receiptprocessor.challenge.model.Receipt;
import com.receiptprocessor.challenge.model.ReceiptItem;
import com.receiptprocessor.challenge.repository.ReceiptRepository;
import com.receiptprocessor.challenge.service.ReceiptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ReceiptServiceTest {

    @Mock
    private ReceiptRepository receiptRepository;

    @InjectMocks
    private ReceiptService receiptService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // This testcase should throw exception when processReceipt is called because fields of Receipt are null.
    @Test
    void testProcessReceiptNegativeRetailer() {
        Receipt receipt = new Receipt();

        // Expect ResponseStatusException with BAD_REQUEST
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            receiptService.processReceipt(receipt);
        });

        // Verify exception status
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    // This testcase should throw exception when processReceipt is called because Total is invalid.
    @Test
    void testProcessReceiptNegativeTotal() {
        // Create a receipt with a negative total
        Receipt receipt = new Receipt();
        receipt.setRetailer("Target");
        receipt.setTotal("-10.00"); // Negative total
        ReceiptItem item1 = new ReceiptItem();
        item1.setShortDescription("Mountain Dew 12PK");
        item1.setPrice("1");
        receipt.setItems(List.of(item1));

        // Expect ResponseStatusException with BAD_REQUEST
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            receiptService.processReceipt(receipt);
        });

        // Verify exception status
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode()); // Ensure correct status
    }

    //Checks if for a valid Receipt, id is generated and stored in map.
    @Test
    void testProcessReceiptValidReceiptReturnReceiptId() {
        Receipt receipt = createSampleReceipt();

        // Process the receipt
        String receiptId = receiptService.processReceipt(receipt);

        // Verify receipt ID is generated and is in UUID format
        assertNotNull(receiptId);
        assertTrue(receiptId.matches("^[0-9a-fA-F-]{36}$"), "Receipt ID should be a valid UUID");

        // Verify repository interaction (ensuring saveReceipt is called once)
        verify(receiptRepository, times(1)).saveReceipt(eq(receiptId), eq(receipt));
    }

    //Checks for a given correct receipt, points are calculated correctly.
    @Test
    void testGetPointsValidReceiptCalculatePoints() {
        Receipt receipt = createSampleReceipt();
        String receiptId = UUID.randomUUID().toString();

        when(receiptRepository.findReceiptById(receiptId)).thenReturn(receipt);
        int points = receiptService.getPoints(receiptId);

        assertTrue(points > 0);
        assertEquals(31,points);
    }

    // Checks for a invalid receipt id, not stored in map, exception is thrown.
    @Test
    void testGetPointsInvalidReceiptIdNegative() {
        String invalidReceiptId = "non-existent-id";
        when(receiptRepository.findReceiptById(invalidReceiptId)).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> receiptService.getPoints(invalidReceiptId));

        // Verify exception status
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode()); // Ensure correct status
    }

    // Checks for a invalid receipt id - null, exception is thrown.
    @Test
    void testGetPointsNullReceiptIdNegative() {
        String invalidReceiptId = null;

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> receiptService.getPoints(invalidReceiptId));

        // Verify exception status
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode()); // Ensure correct status
    }

    //Sample valid receipt for testing purpose.
    private Receipt createSampleReceipt() {
        Receipt receipt = new Receipt();
        receipt.setRetailer("Target");
        receipt.setPurchaseDate("2022-01-02");
        receipt.setPurchaseTime("13:13");
        receipt.setTotal("1.25");

        ReceiptItem item1 = new ReceiptItem();
        item1.setShortDescription("Pepsi - 12-oz");
        item1.setPrice("1.25");

        receipt.setItems(List.of(item1));

        return receipt;
    }
}


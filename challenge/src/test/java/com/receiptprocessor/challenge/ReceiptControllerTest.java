package com.receiptprocessor.challenge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.receiptprocessor.challenge.model.Receipt;
import com.receiptprocessor.challenge.model.ReceiptItem;
import com.receiptprocessor.challenge.service.ReceiptService;
import com.receiptprocessor.challenge.controller.ReceiptController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class ReceiptControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReceiptService receiptService;

    @InjectMocks
    private ReceiptController receiptController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(receiptController).build();
    }

    // Checks for a valid Receipt, receipt and receipt id is stored and correct receipt id is returned.
    @Test
    void testProcessReceiptReturnReceiptIdPositive() throws Exception {
        Receipt receipt = new Receipt();
        receipt.setRetailer("Target");
        receipt.setPurchaseDate("2022-01-02");
        receipt.setPurchaseTime("13:13");
        receipt.setTotal("1.25");

        // Add some items
        ReceiptItem item1 = new ReceiptItem();
        item1.setShortDescription("Pepsi - 12-oz");
        item1.setPrice("1.25");
        receipt.setItems(List.of(item1));
        String receiptId = UUID.randomUUID().toString();

        when(receiptService.processReceipt(any(Receipt.class))).thenReturn(receiptId);

        mockMvc.perform(post("/receipts/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(receipt)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(receiptId));
    }

    // Checks Bad request 400 message is returned from controller when an exception thrown from service class for empty field of Receipt.
    @Test
    void testProcessReceiptInvalidRequestNegative() throws Exception {
        // Sending an empty JSON request
        mockMvc.perform(post("/receipts/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))  // Invalid JSON (missing required fields)
                .andExpect(status().isBadRequest());
    }

    // Checks when receiptService returns points for a receipt, then exception is not thrown and points are returned from controller.
    @Test
    void testGetPointsValidReceiptReturnPointsPositive() throws Exception {
        String receiptId = UUID.randomUUID().toString();
        when(receiptService.getPoints(receiptId)).thenReturn(100);

        mockMvc.perform(get("/receipts/{id}/points", receiptId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.points").value(100));
    }

    // When user requests getPoints for invalid receipt id, then exception is thrown from service class, so controller should return Not Found message.
    @Test
    void testGetPointsInvalidReceiptNegative() throws Exception {
        String invalidReceiptId = "invalid-id";
        when(receiptService.getPoints(invalidReceiptId)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "No receipt found for that ID."));

        mockMvc.perform(get("/receipts/{id}/points", invalidReceiptId))
                .andExpect(status().isNotFound());
    }

    // When user requests getPoints for null receipt id, then exception is thrown from service class, so controller should return Not Found message.
    @Test
    void testGetPointsMissingReceiptIdNegative() throws Exception {
        mockMvc.perform(get("/receipts//points")) // Missing ID in URL
                .andExpect(status().isNotFound());
    }
}

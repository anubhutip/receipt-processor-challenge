package com.receiptprocessor.challenge.controller;

import com.receiptprocessor.challenge.model.Receipt;
import com.receiptprocessor.challenge.service.ReceiptService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/receipts")
public class ReceiptController {

    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    // Endpoint to process receipts
    @PostMapping("/process")
    public ResponseEntity<Map<String, String>> processReceipt(@RequestBody @Valid Receipt receipt) {
        try{
            String receiptId = receiptService.processReceipt(receipt);
            return ResponseEntity.ok(Map.of("id", receiptId));
        } catch (Exception e) {
            // Handle invalid input
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }

    }

    // Endpoint to get points for a receipt
    @GetMapping("/{id}/points")
    public ResponseEntity<Map<String, Integer>> getPoints(@PathVariable String id) {
        try {
            int points = receiptService.getPoints(id);
            return ResponseEntity.ok(Map.of("points", points));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("points", -1)); // Use -1 to indicate an error
        }
    }

}


package com.receiptprocessor.challenge.service;

import com.receiptprocessor.challenge.model.Receipt;
import com.receiptprocessor.challenge.model.ReceiptItem;
import com.receiptprocessor.challenge.repository.ReceiptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class ReceiptService {

    private final ReceiptRepository receiptRepository;

    @Autowired
    public ReceiptService(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    /**
     * For a given receipt, check if it is a valid receipt and then generate receiptId and store in map. Return BAD_REQUEST if receipt is invalid.
     * @param receipt
     * @return receiptId
     */
    public String processReceipt(Receipt receipt) {
        if (receipt == null || receipt.getRetailer() == null || receipt.getTotal() == null || receipt.getItems() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The receipt is invalid.");
        }

        // Validate and parse total
        try {
            double total = Double.parseDouble(receipt.getTotal());
            if (total < 0) { // Optional: Reject negative totals
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The receipt is invalid. Total cannot be negative.");
            }
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The receipt is invalid. Total must be a valid number.");
        }

        //If valid create receiptId
        String receiptId = UUID.randomUUID().toString();

        // If valid, store receipt in the repository
        receiptRepository.saveReceipt(receiptId, receipt);

        return receiptId;
    }

    /**
     * For a given receiptId, find the receipt and calculate points. Return NOT_FOUND if receipt is not found.
     * @param receiptId
     * @return points
     */
    public int getPoints(String receiptId) {

        Receipt receipt = receiptRepository.findReceiptById(receiptId);
        if (receipt == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No receipt found for that ID.");
        }
        return calculatePoints(receipt);
    }

    /**
     * Calculates points for a receipt according to the given rules.
     * @param receipt
     * @return points
     */
    private int calculatePoints(Receipt receipt) {
        int points = 0;

        // Rule 1: One point for every alphanumeric character in the retailer name
        points += receipt.getRetailer().replaceAll("[^a-zA-Z0-9]", "").length();

        // Rule 2: 50 points if the total is a round dollar amount with no cents
        double total = Double.parseDouble(receipt.getTotal());
        if (total == Math.floor(total)) {
            points += 50;
        }

        // Rule 3: 25 points if the total is a multiple of 0.25
        if (total % 0.25 == 0) {
            points += 25;
        }

        // Rule 4: 5 points for every two items on the receipt
        points += (receipt.getItems().size() / 2) * 5;

        // Rule 5: Item description length multiple of 3
        for (ReceiptItem item : receipt.getItems()) {
            String description = item.getShortDescription().trim();
            if (description.length() % 3 == 0) {
                double itemPrice = Double.parseDouble(item.getPrice());
                points += Math.ceil(itemPrice * 0.2);
            }
        }

        // Rule 6: 6 points if the purchase day is odd
        int day = Integer.parseInt(receipt.getPurchaseDate().split("-")[2]);
        if (day % 2 == 1) {
            points += 6;
        }

        // Rule 7: 10 points if purchase time is between 2:00 PM and 4:00 PM
        String[] timeParts = receipt.getPurchaseTime().split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);
        if ((hour == 14 && minute>=1 ) || (hour == 15 && minute<=59)) {
            points += 10;
        }

        return points;
    }
}

package com.example.services;
import java.util.List;
import java.util.Optional;

import com.example.entities.Receipt;



public interface ReceiptService {
    Optional<Receipt> getReceiptById(int receiptId);
    List<Receipt> getAllReceipts();
    Receipt addReceipt(Receipt receipt);
}
package com.aegis.transaction.controller;

import com.aegis.transaction.dto.TransactionRequest;
import com.aegis.transaction.model.Transaction;
import com.aegis.transaction.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    @PostMapping
    public ResponseEntity<Transaction> submitTransaction(@RequestBody TransactionRequest request) {
        Transaction tx = transactionService.processTransaction(request);
        return ResponseEntity.ok(tx);
    }
}

package com.pab.bankb.controller;

import com.pab.bankb.dto.TransactionForBankDto;
import com.pab.bankb.dto.TransactionForClientDto;
import com.pab.bankb.dto.TransferRequest;
import com.pab.bankb.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("api/transaction")
public class TransactionController {

    private TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/send-transfer")
    ResponseEntity<Void> sendTransfer(@RequestBody TransferRequest transferRequest) {
        this.transactionService.sendTransfer(transferRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/account/{id}")
    ResponseEntity<List<TransactionForClientDto>> getTransactionListForAccount(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(transactionService.getTransactionListForAccount(id));
    }

    @GetMapping("/all-transaction")
    ResponseEntity<List<TransactionForBankDto>> getAllTransactionList() {
        return ResponseEntity.status(HttpStatus.OK).body(transactionService.getTransactionListForBank());
    }

    @GetMapping("/synchronization-with-the-accounting-unit")
    ResponseEntity<Void> synchronizationWithTheAccountingUnit() {
        this.transactionService.synchronizationWithTheAccountingUnit();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

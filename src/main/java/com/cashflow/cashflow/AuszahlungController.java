package com.cashflow.cashflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/auszahlungen")
public class AuszahlungController {

    @Autowired
    private AuszahlungService auszahlungService;

    // Get all transactions
    @GetMapping
    public List<Auszahlung> getAllTransactions() {
        return auszahlungService.getAllTransactions();
    }

    // Add new transaction
    @PostMapping
    public Auszahlung addTransaction(@RequestBody Auszahlung newTransaction) {
        return auszahlungService.addTransaction(newTransaction);
    }

    // Edit transaction
    @PutMapping("/{id}")
    public Auszahlung updateTransaction(@PathVariable UUID id, @RequestBody Auszahlung updatedTransaction) {
        return auszahlungService.updateTransaction(id, updatedTransaction);
    }

    // Delete transaction
    @DeleteMapping("/{id}")
    public String deleteTransaction(@PathVariable UUID id) {
        auszahlungService.deleteTransaction(id);
        return "Transaktion gelöscht!";
    }
}

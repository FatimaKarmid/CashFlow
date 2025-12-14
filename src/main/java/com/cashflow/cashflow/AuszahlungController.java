package com.cashflow.cashflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<Auszahlung>> getAllTransactions() {
        try {
            return ResponseEntity.ok(auszahlungService.getAllTransactions());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Add new transaction
    @PostMapping
    public ResponseEntity<Auszahlung> addTransaction(@RequestBody Auszahlung newTransaction) {
        try {
            System.out.println("Empfangene Transaktion: " + newTransaction);
            return ResponseEntity.status(HttpStatus.CREATED).body(auszahlungService.addTransaction(newTransaction));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    // Edit transaction
    @PutMapping("/{id}")
    public ResponseEntity<Auszahlung> updateTransaction(@PathVariable UUID id, @RequestBody Auszahlung updatedTransaction) {
        try {
            return ResponseEntity.ok(auszahlungService.updateTransaction(id, updatedTransaction));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Delete transaction
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTransaction(@PathVariable UUID id) {
        try {
            auszahlungService.deleteTransaction(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Transaktion gelöscht");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transaktion nicht gefunden");
        }
    }
}

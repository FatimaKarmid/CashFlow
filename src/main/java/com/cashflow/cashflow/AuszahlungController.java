package com.cashflow.cashflow;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.math.BigDecimal;
import java.time.LocalDate;

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

    @GetMapping("/summe-monat")
    public ResponseEntity<BigDecimal> summeProMonat(
            @RequestParam int monat,
            @RequestParam int jahr) {
        return ResponseEntity.ok(auszahlungService.getSummeProMonat(monat, jahr));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Auszahlung>> filterByKategorie(
            @RequestParam Auszahlung.Verwendungszweck kategorie) {
        return ResponseEntity.ok(auszahlungService.getByKategorie(kategorie));
    }

    @GetMapping("/summe")
    public ResponseEntity<BigDecimal> getSummeAmTag(@RequestParam LocalDate datum) {
        try {
            return ResponseEntity.ok(auszahlungService.getSummeAmTag(datum));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/chart")
    public Map<String, BigDecimal> chartDaten(
            @RequestParam int monat,
            @RequestParam int jahr) {
        return auszahlungService.summeNachKategorie(monat, jahr);
    }

    @GetMapping(params = "datum")
    public List<Auszahlung> getByDatum(@RequestParam LocalDate datum) {
        return auszahlungService.getByDatum(datum);
    }

    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody Auszahlung a) {
        return ResponseEntity.ok(auszahlungService.addTransaction(a));
    }

}

package com.cashflow.cashflow;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auszahlungen")
@CrossOrigin(
        origins = {
                "http://localhost:5173",
                "https://cashflow-frontend-vspq.onrender.com"
        }
)
public class AuszahlungController {

    private final AuszahlungService auszahlungService;

    public AuszahlungController(AuszahlungService auszahlungService) {
        this.auszahlungService = auszahlungService;
    }

    // Alle Auszahlungen (ohne Filter)
    @GetMapping
    public ResponseEntity<List<Auszahlung>> getAll() {
        return ResponseEntity.ok(
                auszahlungService.getAllTransactions()
        );
    }

    // EIN Filter-Endpunkt für ALLE Kombinationen
    // Name + Kategorie + Datum (beliebig kombinierbar)
    @GetMapping("/filter")
    public ResponseEntity<List<Auszahlung>> filter(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) LocalDate datum,
            @RequestParam(required = false) Auszahlung.Verwendungszweck kategorie
    ) {
        return ResponseEntity.ok(
                auszahlungService.filter(name, datum, kategorie)
        );
    }

    // Summe pro Tag
    @GetMapping("/summe")
    public ResponseEntity<BigDecimal> summeTag(
            @RequestParam LocalDate datum
    ) {
        return ResponseEntity.ok(
                auszahlungService.getSummeAmTag(datum)
        );
    }

    // Summe pro Monat
    @GetMapping("/summe-monat")
    public ResponseEntity<BigDecimal> summeMonat(
            @RequestParam int monat,
            @RequestParam int jahr
    ) {
        return ResponseEntity.ok(
                auszahlungService.getSummeProMonat(monat, jahr)
        );
    }

    // Chart-Daten (Summe je Kategorie)
    @GetMapping("/chart")
    public ResponseEntity<Map<String, BigDecimal>> chart(
            @RequestParam int monat,
            @RequestParam int jahr
    ) {
        return ResponseEntity.ok(
                auszahlungService.summeNachKategorie(monat, jahr)
        );
    }

    // Neue Auszahlung
    @PostMapping
    public ResponseEntity<Auszahlung> add(
            @Valid @RequestBody Auszahlung auszahlung
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(auszahlungService.addTransaction(auszahlung));
    }

    // Update bestehender Auszahlung
    @PutMapping("/{id}")
    public ResponseEntity<Auszahlung> update(
            @PathVariable UUID id,
            @Valid @RequestBody Auszahlung updated
    ) {
        return ResponseEntity.ok(
                auszahlungService.updateTransaction(id, updated)
        );
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id
    ) {
        auszahlungService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}

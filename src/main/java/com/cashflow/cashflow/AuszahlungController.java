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

    // Constructor Injection (Best Practice)
    public AuszahlungController(AuszahlungService auszahlungService) {
        this.auszahlungService = auszahlungService;
    }

    // GET
    // Alle Auszahlungen
    @GetMapping
    public ResponseEntity<List<Auszahlung>> getAll() {
        return ResponseEntity.ok(auszahlungService.getAllTransactions());
    }

    // Filter nach Kategorie und/oder Datum
    @GetMapping("/filter")
    public ResponseEntity<List<Auszahlung>> filter(
            @RequestParam(required = false) Auszahlung.Verwendungszweck kategorie,
            @RequestParam(required = false) LocalDate datum,
            @RequestParam(required = false) Auszahlung.Zahlungsart zahlungsart) {

        // Filter nach Kategorie und Zahlungsart
        if (kategorie != null && zahlungsart != null) {
            return ResponseEntity.ok(auszahlungService.getByZahlungsartAndKategorie(zahlungsart, kategorie));
        }
        // Filter nach Kategorie und Datum
        else if (kategorie != null && datum != null) {
            return ResponseEntity.ok(auszahlungService.getByKategorieAndDatum(kategorie, datum));
        }
        // Filter nach Datum und Zahlungsart
        else if (datum != null && zahlungsart != null) {
            return ResponseEntity.ok(auszahlungService.getByZahlungsartAndDatum(zahlungsart, datum));
        }
        // Nur nach Kategorie filtern
        else if (kategorie != null) {
            return ResponseEntity.ok(auszahlungService.getByKategorie(kategorie));
        }
        // Nur nach Datum filtern
        else if (datum != null) {
            return ResponseEntity.ok(auszahlungService.getByDatum(datum));
        }
        // Nur nach Zahlungsart filtern
        else if (zahlungsart != null) {
            return ResponseEntity.ok(auszahlungService.getByZahlungsart(zahlungsart));
        }
        // Alle Auszahlungen, wenn keine Filter angewendet werden
        else {
            return ResponseEntity.ok(auszahlungService.getAllTransactions());
        }
    }

    // Summe pro Tag
    @GetMapping("/summe")
    public ResponseEntity<BigDecimal> summeTag(
            @RequestParam LocalDate datum) {
        return ResponseEntity.ok(auszahlungService.getSummeAmTag(datum));
    }

    // Summe pro Monat
    @GetMapping("/summe-monat")
    public ResponseEntity<BigDecimal> summeMonat(
            @RequestParam int monat,
            @RequestParam int jahr) {
        return ResponseEntity.ok(auszahlungService.getSummeProMonat(monat, jahr));
    }

    // Chart: Summe nach Kategorie
    @GetMapping("/chart")
    public ResponseEntity<Map<String, BigDecimal>> chart(
            @RequestParam int monat,
            @RequestParam int jahr) {
        return ResponseEntity.ok(auszahlungService.summeNachKategorie(monat, jahr));
    }

    // POST
    // Neue Auszahlung
    @PostMapping
    public ResponseEntity<Auszahlung> add(
            @Valid @RequestBody Auszahlung auszahlung) {
        Auszahlung saved = auszahlungService.addTransaction(auszahlung);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<Auszahlung> update(
            @PathVariable UUID id,
            @Valid @RequestBody Auszahlung updated) {
        Auszahlung saved = auszahlungService.updateTransaction(id, updated);
        return ResponseEntity.ok(saved);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        auszahlungService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }


}

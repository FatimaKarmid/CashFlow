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

    // Filter für Auszahlungen
    @GetMapping("/filter")
    public ResponseEntity<List<Auszahlung>> filter(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String datum,  // Datum als String empfangen
            @RequestParam(required = false) Auszahlung.Verwendungszweck kategorie
    ) {
        // Bereinigung des Namens-Parameters
        String safeName = (name == null || name.isBlank()) ? null : name.trim();

        // Datum verarbeiten, falls es nicht null ist
        LocalDate parsedDatum = null;
        if (datum != null && !datum.isBlank()) {
            try {
                parsedDatum = LocalDate.parse(datum);  // Umwandlung von String zu LocalDate
            } catch (Exception e) {
                // Falls das Datum nicht im richtigen Format vorliegt, setzen wir es auf null
                parsedDatum = null;
            }
        }

        // Aufruf der Service-Methode mit dem korrekten Datum
        return ResponseEntity.ok(
                auszahlungService.filter(safeName, parsedDatum, kategorie)
        );
    }

    // Summe der Auszahlungen für ein bestimmtes Datum
    @GetMapping("/summe")
    public ResponseEntity<BigDecimal> summeTag(
            @RequestParam LocalDate datum
    ) {
        return ResponseEntity.ok(
                auszahlungService.getSummeAmTag(datum)
        );
    }

    // Summe der Auszahlungen für einen bestimmten Monat und Jahr
    @GetMapping("/summe-monat")
    public ResponseEntity<BigDecimal> summeMonat(
            @RequestParam int monat,
            @RequestParam int jahr
    ) {
        return ResponseEntity.ok(
                auszahlungService.getSummeProMonat(monat, jahr)
        );
    }

    // Diagramm mit Ausgaben pro Kategorie für einen Monat und Jahr
    @GetMapping("/chart")
    public ResponseEntity<Map<String, BigDecimal>> chart(
            @RequestParam int monat,
            @RequestParam int jahr
    ) {
        return ResponseEntity.ok(
                auszahlungService.summeNachKategorie(monat, jahr)
        );
    }

    // Neue Auszahlung hinzufügen
    @PostMapping
    public ResponseEntity<Auszahlung> add(
            @Valid @RequestBody Auszahlung auszahlung
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(auszahlungService.addTransaction(auszahlung));
    }

    // Eine bestehende Auszahlung aktualisieren
    @PutMapping("/{id}")
    public ResponseEntity<Auszahlung> update(
            @PathVariable UUID id,
            @Valid @RequestBody Auszahlung updated
    ) {
        return ResponseEntity.ok(
                auszahlungService.updateTransaction(id, updated)
        );
    }

    // Eine Auszahlung löschen
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id
    ) {
        auszahlungService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}

package com.cashflow.cashflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
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

    @Autowired
    private AuszahlungService auszahlungService;


    // Alle Auszahlungen
    @GetMapping
    public List<Auszahlung> getAll() {
        return auszahlungService.getAllTransactions();
    }

    // Filter nach Kategorie
    @GetMapping("/filter")
    public List<Auszahlung> filterByKategorie(
            @RequestParam Auszahlung.Verwendungszweck kategorie) {
        return auszahlungService.getByKategorie(kategorie);
    }

    // Filter nach Datum
    @GetMapping(params = "datum")
    public List<Auszahlung> filterByDatum(
            @RequestParam LocalDate datum) {
        return auszahlungService.getByDatum(datum);
    }

    // Summe pro Tag
    @GetMapping("/summe")
    public BigDecimal summeTag(@RequestParam LocalDate datum) {
        return auszahlungService.getSummeAmTag(datum);
    }

    // Summe pro Monat
    @GetMapping("/summe-monat")
    public BigDecimal summeMonat(
            @RequestParam int monat,
            @RequestParam int jahr) {
        return auszahlungService.getSummeProMonat(monat, jahr);
    }

    // Chart: Summe nach Kategorie
    @GetMapping("/chart")
    public Map<String, BigDecimal> chart(
            @RequestParam int monat,
            @RequestParam int jahr) {
        return auszahlungService.summeNachKategorie(monat, jahr);
    }

    // NEUE AUSZAHLUNG
    @PostMapping
    public ResponseEntity<Auszahlung> add(
            @Valid @RequestBody Auszahlung auszahlung) {

        Auszahlung saved = auszahlungService.addTransaction(auszahlung);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }


    @PutMapping("/{id}")
    public Auszahlung update(
            @PathVariable UUID id,
            @RequestBody Auszahlung updated) {

        return auszahlungService.updateTransaction(id, updated);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        auszahlungService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}

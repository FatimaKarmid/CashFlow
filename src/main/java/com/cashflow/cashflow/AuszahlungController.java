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
public class AuszahlungController {

    @Autowired
    private AuszahlungService auszahlungService;

    // ALLE
    @GetMapping
    public List<Auszahlung> getAll() {
        return auszahlungService.getAllTransactions();
    }

    // POST (NUR EINMAL!)
    @PostMapping
    public ResponseEntity<Auszahlung> add(@Valid @RequestBody Auszahlung auszahlung) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(auszahlungService.addTransaction(auszahlung));
    }

    // UPDATE
    @PutMapping("/{id}")
    public Auszahlung update(
            @PathVariable UUID id,
            @RequestBody Auszahlung updated) {
        return auszahlungService.updateTransaction(id, updated);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        auszahlungService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    // FILTER KATEGORIE
    @GetMapping("/filter")
    public List<Auszahlung> filterByKategorie(
            @RequestParam Auszahlung.Verwendungszweck kategorie) {
        return auszahlungService.getByKategorie(kategorie);
    }

    // FILTER DATUM
    @GetMapping(params = "datum")
    public List<Auszahlung> filterByDatum(@RequestParam LocalDate datum) {
        return auszahlungService.getByDatum(datum);
    }

    // SUMME TAG
    @GetMapping("/summe")
    public BigDecimal summeTag(@RequestParam LocalDate datum) {
        return auszahlungService.getSummeAmTag(datum);
    }

    // SUMME MONAT
    @GetMapping("/summe-monat")
    public BigDecimal summeMonat(
            @RequestParam int monat,
            @RequestParam int jahr) {
        return auszahlungService.getSummeProMonat(monat, jahr);
    }

    // CHART
    @GetMapping("/chart")
    public Map<String, BigDecimal> chart(
            @RequestParam int monat,
            @RequestParam int jahr) {
        return auszahlungService.summeNachKategorie(monat, jahr);
    }
}

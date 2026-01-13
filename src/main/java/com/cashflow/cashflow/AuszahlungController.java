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

    // Alle Auszahlungen
    @GetMapping
    public ResponseEntity<List<Auszahlung>> getAll() {
        return ResponseEntity.ok(auszahlungService.getAllTransactions());
    }

    // 🔹 EIN Filter-Endpunkt: Kategorie + Datum
    @GetMapping("/filter")
    public ResponseEntity<List<Auszahlung>> filter(
            @RequestParam(required = false) Auszahlung.Verwendungszweck kategorie,
            @RequestParam(required = false) LocalDate datum
    ) {
        if (kategorie != null && datum != null) {
            return ResponseEntity.ok(
                    auszahlungService.getByKategorieAndDatum(kategorie, datum)
            );
        }
        if (kategorie != null) {
            return ResponseEntity.ok(
                    auszahlungService.getByKategorie(kategorie)
            );
        }
        if (datum != null) {
            return ResponseEntity.ok(
                    auszahlungService.getByDatum(datum)
            );
        }
        return ResponseEntity.ok(
                auszahlungService.getAllTransactions()
        );
    }

    // Dashboard
    @GetMapping("/summe")
    public ResponseEntity<BigDecimal> summeTag(@RequestParam LocalDate datum) {
        return ResponseEntity.ok(auszahlungService.getSummeAmTag(datum));
    }

    @GetMapping("/summe-monat")
    public ResponseEntity<BigDecimal> summeMonat(
            @RequestParam int monat,
            @RequestParam int jahr
    ) {
        return ResponseEntity.ok(auszahlungService.getSummeProMonat(monat, jahr));
    }

    @GetMapping("/chart")
    public ResponseEntity<Map<String, BigDecimal>> chart(
            @RequestParam int monat,
            @RequestParam int jahr
    ) {
        return ResponseEntity.ok(auszahlungService.summeNachKategorie(monat, jahr));
    }

    @PostMapping
    public ResponseEntity<Auszahlung> add(@Valid @RequestBody Auszahlung auszahlung) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(auszahlungService.addTransaction(auszahlung));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        auszahlungService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}

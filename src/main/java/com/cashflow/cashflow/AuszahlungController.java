package com.cashflow.cashflow;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.http.ResponseEntity;

@RestController
public class AuszahlungController {
    private final AuszahlungRepository auszahlungRepository;

    public AuszahlungController(AuszahlungRepository auszahlungRepository) {
        this.auszahlungRepository = auszahlungRepository;
    }

    // GET Route: Alle Transaktionen abrufen
    @GetMapping("/auszahlungen")
    public ResponseEntity<List<Auszahlung>> getAllTransactions() {
        List<Auszahlung> auszahlungen = auszahlungRepository.findAll();
        if (auszahlungen.isEmpty()) {
            return ResponseEntity.notFound().build(); // Gibt 404 zurück, wenn keine Auszahlungen vorhanden sind
        }
        return ResponseEntity.ok(auszahlungen); // Gibt 200 OK zurück mit den Auszahlungen
    }


    // POST Route: Neue Transaktion hinzufügen
    @PostMapping("/auszahlungen")
    public Auszahlung addTransaction(@RequestBody Auszahlung transaction) {
        return auszahlungRepository.save(transaction);  // Speichert die neue Transaktion in der DB
    }
}


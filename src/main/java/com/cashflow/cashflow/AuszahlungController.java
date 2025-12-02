package com.cashflow.cashflow;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class AuszahlungController {
    private final AuszahlungRepository auszahlungRepository;

    public AuszahlungController(AuszahlungRepository auszahlungRepository) {
        this.auszahlungRepository = auszahlungRepository;
    }

    // GET Route: Alle Transaktionen abrufen
    @GetMapping("/auszahlungen")
    public List<Auszahlung> getAllTransactions() {
        return auszahlungRepository.findAll();  // Gibt alle Transaktionen zurück
    }

    // POST Route: Neue Transaktion hinzufügen
    @PostMapping("/auszahlungen")
    public Auszahlung addTransaction(@RequestBody Auszahlung transaction) {
        return auszahlungRepository.save(transaction);  // Speichert die neue Transaktion in der DB
    }
}


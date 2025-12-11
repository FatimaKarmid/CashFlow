package com.cashflow.cashflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;

@RestController
public class AuszahlungController {
    private final AuszahlungRepository auszahlungRepository;

    @Autowired
    public AuszahlungController(AuszahlungRepository auszahlungRepository) {
        this.auszahlungRepository = auszahlungRepository;
    }

    // GET Route: Alle Transaktionen abrufen
    @GetMapping("/auszahlungen")
    public List<Auszahlung> getAll() {
        return new ArrayList<>((Collection<? extends Auszahlung>) auszahlungRepository.findAll());
    }

    // GET Route: Auszahlungen für ein bestimmtes Datum
    @GetMapping("/auszahlungen/{datum}")
    public List<Auszahlung> getByDatum(@PathVariable LocalDate datum) {
        return auszahlungRepository.findByDatum(datum);
    }

    // POST Route: Neue Transaktion hinzufügen
    @PostMapping("/auszahlungen")
    public ResponseEntity<Auszahlung> addTransaction(@RequestBody Auszahlung transaction) {
        Auszahlung saved = auszahlungRepository.save(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved); // 201 Created
    }
}


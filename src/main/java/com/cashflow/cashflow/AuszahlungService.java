package com.cashflow.cashflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class AuszahlungService {

    @Autowired
    private AuszahlungRepository auszahlungRepository;

    // Alle Transaktionen
    public List<Auszahlung> getAllTransactions() {
        return auszahlungRepository.findAll();
    }

    // Summe pro Monat
    public BigDecimal getSummeProMonat(int monat, int jahr) {
        return auszahlungRepository.summeProMonat(monat, jahr);
    }

    // Filter nach Kategorie
    public List<Auszahlung> getByKategorie(Auszahlung.Verwendungszweck verwendungszweck) {
        return auszahlungRepository.findByVerwendungszweck(verwendungszweck);
    }

    // Summe pro Tag
    public BigDecimal getSummeAmTag(LocalDate datum) {
        return auszahlungRepository.summeAmTag(datum);
    }

    // Filter nach Datum
    public List<Auszahlung> getByDatum(LocalDate datum) {
        return auszahlungRepository.findByDatum(datum);
    }

    // Neue Transaktion
    public Auszahlung addTransaction(Auszahlung newTransaction) {
        return auszahlungRepository.save(newTransaction);
    }

    // Update
    public Auszahlung updateTransaction(UUID id, Auszahlung updatedTransaction) {
        Optional<Auszahlung> existingTransaction = auszahlungRepository.findById(id);
        if (existingTransaction.isPresent()) {
            updatedTransaction.setId(id);
            return auszahlungRepository.save(updatedTransaction);
        } else {
            throw new RuntimeException("Transaktion nicht gefunden");
        }
    }

    // Löschen
    public void deleteTransaction(UUID id) {
        auszahlungRepository.deleteById(id);
    }

    // Chart-Daten
    public Map<String, BigDecimal> summeNachKategorie(int monat, int jahr) {
        Map<String, BigDecimal> result = new HashMap<>();
        for (Object[] row : auszahlungRepository.summeNachKategorie(monat, jahr)) {
            result.put(row[0].toString(), (BigDecimal) row[1]);
        }
        return result;
    }
}

package com.cashflow.cashflow;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class AuszahlungService {

    private final AuszahlungRepository auszahlungRepository;

    public AuszahlungService(AuszahlungRepository auszahlungRepository) {
        this.auszahlungRepository = auszahlungRepository;
    }

    // Alle Auszahlungen (sortiert nach Datum)
    public List<Auszahlung> getAllTransactions() {
        return auszahlungRepository.findAll(
                Sort.by(Sort.Direction.DESC, "datum")
        );
    }

    // Filter für Auszahlungen
    public List<Auszahlung> filter(
            String name,
            LocalDate datum,
            Auszahlung.Verwendungszweck kategorie
    ) {
        // Leere oder nur aus Leerzeichen bestehende Namen bereinigen
        String safeName = (name == null || name.isBlank()) ? null : name.trim();

        // Falls das Datum null ist, behandeln wir es als optional (keine Filterung auf das Datum)
        if (datum == null) {
            return auszahlungRepository.filter(safeName, null, kategorie);
        } else {
            return auszahlungRepository.filter(safeName, datum, kategorie);
        }
    }

    // Neue Auszahlung hinzufügen
    public Auszahlung addTransaction(Auszahlung auszahlung) {
        return auszahlungRepository.save(auszahlung);
    }

    // Eine bestehende Auszahlung aktualisieren
    public Auszahlung updateTransaction(UUID id, Auszahlung updated) {
        return auszahlungRepository.findById(id)
                .map(existing -> {
                    updated.setId(id);
                    return auszahlungRepository.save(updated);
                })
                .orElseThrow(() -> new NoSuchElementException(
                        "Auszahlung mit ID " + id + " nicht gefunden"
                ));
    }

    // Eine Auszahlung löschen
    public void deleteTransaction(UUID id) {
        auszahlungRepository.deleteById(id);
    }

    // Summe der Auszahlungen für einen bestimmten Tag
    public BigDecimal getSummeAmTag(LocalDate datum) {
        return auszahlungRepository.summeAmTag(datum);
    }

    // Summe der Auszahlungen für einen bestimmten Monat und Jahr
    public BigDecimal getSummeProMonat(int monat, int jahr) {
        LocalDate start = LocalDate.of(jahr, monat, 1);
        LocalDate ende = start.plusMonths(1);
        return auszahlungRepository.summeProMonat(start, ende);
    }

    // Ausgaben nach Kategorie für einen bestimmten Monat und Jahr
    public Map<String, BigDecimal> summeNachKategorie(int monat, int jahr) {
        LocalDate start = LocalDate.of(jahr, monat, 1);
        LocalDate ende = start.plusMonths(1);

        Map<String, BigDecimal> result = new LinkedHashMap<>();
        for (Object[] row : auszahlungRepository.summeNachKategorie(start, ende)) {
            result.put(
                    row[0].toString(),
                    (BigDecimal) row[1]
            );
        }
        return result;
    }
}

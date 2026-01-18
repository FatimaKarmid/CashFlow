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

    // Alle Auszahlungen, sortiert nach Datum (neueste zuerst)
    public List<Auszahlung> getAllTransactions() {
        return auszahlungRepository.findAll(Sort.by(Sort.Direction.DESC, "datum"));
    }

    // Filter für Auszahlungen (nach Name, Datum und Kategorie)
    public List<Auszahlung> filter(
            String name,
            LocalDate datum,
            Auszahlung.Verwendungszweck kategorie
    ) {
        // Bereinigung des Namens-Parameters
        String safeName = (name == null || name.isBlank()) ? null : name.trim();

        // Falls das Datum null ist, behandeln wir es als optional
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
        // Wenn die Auszahlung mit der gegebenen ID existiert, wird sie aktualisiert
        return auszahlungRepository.findById(id)
                .map(existing -> {
                    updated.setId(id);  // Setzt die ID für das aktualisierte Objekt
                    return auszahlungRepository.save(updated);  // Speichert die Änderung
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
        LocalDate start = LocalDate.of(jahr, monat, 1);  // Erster Tag des Monats
        LocalDate ende = start.plusMonths(1);  // Erster Tag des nächsten Monats
        return auszahlungRepository.summeProMonat(start, ende);
    }

    // Summe der Ausgaben nach Kategorie für einen bestimmten Monat und Jahr
    public Map<String, BigDecimal> summeNachKategorie(int monat, int jahr) {
        LocalDate start = LocalDate.of(jahr, monat, 1);  // Erster Tag des Monats
        LocalDate ende = start.plusMonths(1);  // Erster Tag des nächsten Monats

        Map<String, BigDecimal> result = new LinkedHashMap<>();  // LinkedHashMap für sortierte Einträge
        for (Object[] row : auszahlungRepository.summeNachKategorie(start, ende)) {
            result.put(
                    row[0].toString(),  // Verwendungszweck (Kategorie) als Key
                    (BigDecimal) row[1]  // Summe der Beträge als Value
            );
        }
        return result;
    }
}

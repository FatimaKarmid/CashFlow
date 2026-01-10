package com.cashflow.cashflow;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class AuszahlungService {

    private final AuszahlungRepository auszahlungRepository;

    // Constructor Injection
    public AuszahlungService(AuszahlungRepository auszahlungRepository) {
        this.auszahlungRepository = auszahlungRepository;
    }

    // Filter nach Zahlungsart und Datum
    public List<Auszahlung> getByZahlungsartAndDatum(Auszahlung.Zahlungsart zahlungsart, LocalDate datum) {
        return auszahlungRepository.findByZahlungsartAndDatum(
                zahlungsart, datum, Sort.by(Sort.Direction.DESC, "datum")
        );
    }


    // Alle Transaktionen – SORTIERT (neu → alt)
    public List<Auszahlung> getAllTransactions() {
        return auszahlungRepository.findAll(
                Sort.by(Sort.Direction.DESC, "datum")
        );
    }

    // Neue Transaktion
    public Auszahlung addTransaction(Auszahlung newTransaction) {
        return auszahlungRepository.save(newTransaction);
    }

    // Update
    public Auszahlung updateTransaction(UUID id, Auszahlung updatedTransaction) {
        return auszahlungRepository.findById(id)
                .map(existing -> {
                    updatedTransaction.setId(id);
                    return auszahlungRepository.save(updatedTransaction);
                })
                .orElseThrow(() -> new NoSuchElementException("Transaktion nicht gefunden: " + id));
    }

    // Löschen
    public void deleteTransaction(UUID id) {
        auszahlungRepository.deleteById(id);
    }

    // Nach Kategorie filtern – SORTIERT (neu → alt)
    public List<Auszahlung> getByKategorie(Auszahlung.Verwendungszweck verwendungszweck) {
        return auszahlungRepository.findByVerwendungszweck(
                verwendungszweck,
                Sort.by(Sort.Direction.DESC, "datum")
        );
    }

    // Nach Datum filtern – SORTIERT nach Betrag (optional sinnvoll)
    public List<Auszahlung> getByDatum(LocalDate datum) {
        return auszahlungRepository.findByDatum(
                datum,
                Sort.by(Sort.Direction.DESC, "betrag")
        );
    }

    // Filter nach Kategorie und Datum
    public List<Auszahlung> getByKategorieAndDatum(Auszahlung.Verwendungszweck verwendungszweck, LocalDate datum) {
        return auszahlungRepository.findByVerwendungszweckAndDatum(
                verwendungszweck, datum, Sort.by(Sort.Direction.DESC, "datum")
        );
    }

    // Summe an einem Tag
    public BigDecimal getSummeAmTag(LocalDate datum) {
        return auszahlungRepository.summeAmTag(datum);
    }

    // Summe pro Monat
    public BigDecimal getSummeProMonat(int monat, int jahr) {
        LocalDate start = LocalDate.of(jahr, monat, 1);
        LocalDate ende = start.plusMonths(1);
        return auszahlungRepository.summeProMonat(start, ende);
    }

    // Summe nach Kategorie für Diagramme
    public Map<String, BigDecimal> summeNachKategorie(int monat, int jahr) {
        LocalDate start = LocalDate.of(jahr, monat, 1);
        LocalDate ende = start.plusMonths(1);

        Map<String, BigDecimal> result = new LinkedHashMap<>();

        for (Object[] row : auszahlungRepository.summeNachKategorie(start, ende)) {
            String kategorie = row[0].toString(); // Enum → String
            BigDecimal summe = (BigDecimal) row[1];
            result.put(kategorie, summe);
        }

        return result;
    }

    // Filter nach Zahlungsart
    public List<Auszahlung> getByZahlungsart(Auszahlung.Zahlungsart zahlungsart) {
        return auszahlungRepository.findByZahlungsart(
                zahlungsart,
                Sort.by(Sort.Direction.DESC, "datum")
        );
    }

    // Filter nach Zahlungsart und Kategorie
    public List<Auszahlung> getByZahlungsartAndKategorie(Auszahlung.Zahlungsart zahlungsart, Auszahlung.Verwendungszweck verwendungszweck) {
        return auszahlungRepository.findByZahlungsartAndVerwendungszweck(
                zahlungsart, verwendungszweck,
                Sort.by(Sort.Direction.DESC, "datum")
        );
    }

    // Filter nach Zahlungsart, Kategorie und Datum
    public List<Auszahlung> getByZahlungsartAndKategorieAndDatum(Auszahlung.Zahlungsart zahlungsart, Auszahlung.Verwendungszweck verwendungszweck, LocalDate datum) {
        return auszahlungRepository.findByZahlungsartAndVerwendungszweckAndDatum(
                zahlungsart, verwendungszweck, datum,
                Sort.by(Sort.Direction.DESC, "datum")
        );
    }
}

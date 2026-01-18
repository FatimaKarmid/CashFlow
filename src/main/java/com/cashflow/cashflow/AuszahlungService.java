package com.cashflow.cashflow;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class AuszahlungService {

    private final AuszahlungRepository auszahlungRepository;

    public AuszahlungService(AuszahlungRepository auszahlungRepository) {
        this.auszahlungRepository = auszahlungRepository;
    }


    // Alle Auszahlungen, sortiert nach Datum (neueste zuerst)
    public List<Auszahlung> getAllTransactions() {
        return auszahlungRepository.findAll(
                Sort.by(Sort.Direction.DESC, "datum")
        );
    }

    // ROBUSTER FILTER (PostgreSQL-sicher)
    public List<Auszahlung> filter(
            String name,
            LocalDate datum,
            Auszahlung.Verwendungszweck kategorie
    ) {
        String safeName = (name == null || name.isBlank())
                ? null
                : name.trim();

        if (datum == null) {
            return auszahlungRepository.filterOhneDatum(
                    safeName,
                    kategorie
            );
        }

        LocalDate ende = datum.plusDays(1);

        return auszahlungRepository.filterMitDatum(
                safeName,
                datum,
                ende,
                kategorie
        );
    }


    // Neue Auszahlung hinzufügen
    public Auszahlung addTransaction(Auszahlung auszahlung) {
        return auszahlungRepository.save(auszahlung);
    }

    //  KORREKTES UPDATE (keine ID-Manipulation)
    public Auszahlung updateTransaction(UUID id, Auszahlung updated) {
        return auszahlungRepository.findById(id)
                .map(existing -> {
                    existing.setName(updated.getName());
                    existing.setBetrag(updated.getBetrag());
                    existing.setDatum(updated.getDatum());
                    existing.setZahlungsart(updated.getZahlungsart());
                    existing.setVerwendungszweck(updated.getVerwendungszweck());
                    existing.setNotiz(updated.getNotiz());
                    return auszahlungRepository.save(existing);
                })
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Auszahlung mit ID " + id + " nicht gefunden"
                        )
                );
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

    // Summe der Ausgaben nach Kategorie für einen bestimmten Monat und Jahr
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

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

    //  Alle Auszahlungen (ohne Filter)
    public List<Auszahlung> getAllTransactions() {
        return auszahlungRepository.findAll(
                Sort.by(Sort.Direction.DESC, "datum")
        );
    }

    //  ZENTRALE FILTER-METHODE (alle Kombinationen)
    public List<Auszahlung> filter(
            String name,
            LocalDate datum,
            Auszahlung.Verwendungszweck kategorie
    ) {
        return auszahlungRepository.filter(name, datum, kategorie);
    }

    //  Neue Auszahlung
    public Auszahlung addTransaction(Auszahlung auszahlung) {
        return auszahlungRepository.save(auszahlung);
    }

    //  Update
    public Auszahlung updateTransaction(UUID id, Auszahlung updated) {
        return auszahlungRepository.findById(id)
                .map(existing -> {
                    updated.setId(id);
                    return auszahlungRepository.save(updated);
                })
                .orElseThrow(() ->
                        new NoSuchElementException("Auszahlung mit ID " + id + " nicht gefunden")
                );
    }

    //  Delete
    public void deleteTransaction(UUID id) {
        auszahlungRepository.deleteById(id);
    }

    //  Summe für einen Tag
    public BigDecimal getSummeAmTag(LocalDate datum) {
        return auszahlungRepository.summeAmTag(datum);
    }

    //  Summe für einen Monat
    public BigDecimal getSummeProMonat(int monat, int jahr) {
        LocalDate start = LocalDate.of(jahr, monat, 1);
        LocalDate ende = start.plusMonths(1);
        return auszahlungRepository.summeProMonat(start, ende);
    }

    //  Gruppierte Summen nach Kategorie (Chart)
    public Map<String, BigDecimal> summeNachKategorie(int monat, int jahr) {
        LocalDate start = LocalDate.of(jahr, monat, 1);
        LocalDate ende = start.plusMonths(1);

        Map<String, BigDecimal> result = new LinkedHashMap<>();
        for (Object[] row : auszahlungRepository.summeNachKategorie(start, ende)) {
            result.put(row[0].toString(), (BigDecimal) row[1]);
        }
        return result;
    }
}

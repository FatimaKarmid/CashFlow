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

    public List<Auszahlung> getAllTransactions() {
        return auszahlungRepository.findAll(
                Sort.by(Sort.Direction.DESC, "datum")
        );
    }

    public Auszahlung addTransaction(Auszahlung a) {
        return auszahlungRepository.save(a);
    }

    public void deleteTransaction(UUID id) {
        auszahlungRepository.deleteById(id);
    }

    public List<Auszahlung> getByKategorie(Auszahlung.Verwendungszweck kategorie) {
        return auszahlungRepository.findByVerwendungszweck(
                kategorie,
                Sort.by(Sort.Direction.DESC, "datum")
        );
    }

    public List<Auszahlung> getByDatum(LocalDate datum) {
        return auszahlungRepository.findByDatum(
                datum,
                Sort.by(Sort.Direction.DESC, "datum")
        );
    }

    public List<Auszahlung> getByKategorieAndDatum(
            Auszahlung.Verwendungszweck kategorie,
            LocalDate datum
    ) {
        return auszahlungRepository.findByVerwendungszweckAndDatum(
                kategorie,
                datum,
                Sort.by(Sort.Direction.DESC, "datum")
        );
    }

    public BigDecimal getSummeAmTag(LocalDate datum) {
        return auszahlungRepository.summeAmTag(datum);
    }

    public BigDecimal getSummeProMonat(int monat, int jahr) {
        LocalDate start = LocalDate.of(jahr, monat, 1);
        return auszahlungRepository.summeProMonat(start, start.plusMonths(1));
    }

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

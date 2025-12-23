package com.cashflow.cashflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class AuszahlungService {

    @Autowired
    private AuszahlungRepository auszahlungRepository;

    // Get all transactions
    public List<Auszahlung> getAllTransactions() {
        return auszahlungRepository.findAll();
    }

    public BigDecimal getSummeProMonat(int monat, int jahr) {
        return auszahlungRepository.summeProMonat(monat, jahr);
    }

    public List<Auszahlung> getByKategorie(Auszahlung.Verwendungszweck verwendungszweck) {
        return auszahlungRepository.findByVerwendungszweck(verwendungszweck);
    }

    public BigDecimal getSummeAmTag(LocalDate datum) {
        return auszahlungRepository.summeAmTag(datum);
    }

    // Add new transaction
    public Auszahlung addTransaction(Auszahlung newTransaction) {
        return auszahlungRepository.save(newTransaction);
    }

    // Edit transaction
    public Auszahlung updateTransaction(UUID id, Auszahlung updatedTransaction) {
        Optional<Auszahlung> existingTransaction = auszahlungRepository.findById(id);
        if (existingTransaction.isPresent()) {
            updatedTransaction.setId(id);
            return auszahlungRepository.save(updatedTransaction);
        } else {
            throw new RuntimeException("Transaktion nicht gefunden");
        }
    }

    // Delete transaction
    public void deleteTransaction(UUID id) {
        auszahlungRepository.deleteById(id);
    }
}

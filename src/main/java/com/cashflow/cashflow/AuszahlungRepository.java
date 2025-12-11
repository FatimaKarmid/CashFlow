package com.cashflow.cashflow;

import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AuszahlungRepository extends CrudRepository<Auszahlung, UUID> {
    List<Auszahlung> findByDatum(LocalDate datum);
}

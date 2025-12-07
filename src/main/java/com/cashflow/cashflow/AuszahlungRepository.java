package com.cashflow.cashflow;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AuszahlungRepository  extends JpaRepository<Auszahlung, UUID> {
    List<Auszahlung> findByDatum(LocalDate datum);
    List<Auszahlung> findByZahlungsart(String zahlungsart);

}

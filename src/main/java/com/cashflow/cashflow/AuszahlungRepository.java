package com.cashflow.cashflow;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface AuszahlungRepository extends JpaRepository<Auszahlung, UUID> {
}

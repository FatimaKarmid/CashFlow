package com.cashflow.cashflow;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AuszahlungRepository extends JpaRepository<Auszahlung, UUID> {

    // Alle Auszahlungen an einem bestimmten Datum
    List<Auszahlung> findByDatum(LocalDate datum);

    // Alle Auszahlungen einer Kategorie
    List<Auszahlung> findByVerwendungszweck(
            Auszahlung.Verwendungszweck verwendungszweck
    );

    // Summe an einem Tag
    @Query("""
        SELECT COALESCE(SUM(a.betrag), 0)
        FROM Auszahlung a
        WHERE a.datum = :datum
    """)
    BigDecimal summeAmTag(@Param("datum") LocalDate datum);

    // Summe für einen Monat über Datumsbereich – DB-neutral!
    @Query("""
        SELECT COALESCE(SUM(a.betrag), 0)
        FROM Auszahlung a
        WHERE a.datum >= :start
          AND a.datum < :ende
    """)
    BigDecimal summeProMonat(
            @Param("start") LocalDate start,
            @Param("ende") LocalDate ende
    );


    // Summe nach Kategorie z.B. für Diagramme
    @Query("""
        SELECT a.verwendungszweck, COALESCE(SUM(a.betrag), 0)
        FROM Auszahlung a
        WHERE a.datum >= :start
          AND a.datum < :ende
        GROUP BY a.verwendungszweck
    """)
    List<Object[]> summeNachKategorie(
            @Param("start") LocalDate start,
            @Param("ende") LocalDate ende
    );
}

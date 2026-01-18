package com.cashflow.cashflow;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AuszahlungRepository extends JpaRepository<Auszahlung, UUID> {

    //  ROBUSTER FILTER (Name, Datum, Kategorie)
    @Query("""
        SELECT a
        FROM Auszahlung a
        WHERE (:name IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%')))
          AND (
                :datum IS NULL
                OR (a.datum >= :datum AND a.datum < :datumPlusOne)
              )
          AND (:kategorie IS NULL OR a.verwendungszweck = :kategorie)
        ORDER BY a.datum DESC
    """)
    List<Auszahlung> filter(
            @Param("name") String name,
            @Param("datum") LocalDate datum,
            @Param("datumPlusOne") LocalDate datumPlusOne,
            @Param("kategorie") Auszahlung.Verwendungszweck kategorie
    );

    //  Tages-Summe (hier ist = OK, weil Aggregat)
    @Query("""
        SELECT COALESCE(SUM(a.betrag), 0)
        FROM Auszahlung a
        WHERE a.datum = :datum
    """)
    BigDecimal summeAmTag(
            @Param("datum") LocalDate datum
    );

    //  Monats-Summe
    @Query("""
        SELECT COALESCE(SUM(a.betrag), 0)
        FROM Auszahlung a
        WHERE a.datum >= :start AND a.datum < :ende
    """)
    BigDecimal summeProMonat(
            @Param("start") LocalDate start,
            @Param("ende") LocalDate ende
    );

    // Diagramm: Summe pro Kategorie
    @Query("""
        SELECT a.verwendungszweck, COALESCE(SUM(a.betrag), 0)
        FROM Auszahlung a
        WHERE a.datum >= :start AND a.datum < :ende
        GROUP BY a.verwendungszweck
        ORDER BY a.verwendungszweck
    """)
    List<Object[]> summeNachKategorie(
            @Param("start") LocalDate start,
            @Param("ende") LocalDate ende
    );
}

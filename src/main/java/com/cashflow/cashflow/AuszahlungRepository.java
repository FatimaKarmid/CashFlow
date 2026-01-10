package com.cashflow.cashflow;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AuszahlungRepository extends JpaRepository<Auszahlung, UUID> {

    // Alle Auszahlungen an einem bestimmten Datum (Sortierung kommt von außen)
    List<Auszahlung> findByDatum(
            LocalDate datum,
            Sort sort
    );

    // Alle Auszahlungen einer Kategorie (Sortierung kommt von außen)
    List<Auszahlung> findByVerwendungszweck(
            Auszahlung.Verwendungszweck verwendungszweck,
            Sort sort
    );

    // Alle Auszahlungen nach Kategorie und Datum (Sortierung kommt von außen)
    List<Auszahlung> findByVerwendungszweckAndDatum(
            Auszahlung.Verwendungszweck verwendungszweck,
            LocalDate datum,
            Sort sort
    );

    // Summe an einem Tag
    @Query("""
        SELECT COALESCE(SUM(a.betrag), 0)
        FROM Auszahlung a
        WHERE a.datum = :datum
    """)
    BigDecimal summeAmTag(@Param("datum") LocalDate datum);

    // Summe für einen Monat über Datumsbereich
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

    // Summe nach Kategorie (für Diagramme)
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

    // Filter nach Zahlungsart
    List<Auszahlung> findByZahlungsart(Auszahlung.Zahlungsart zahlungsart, Sort sort);

    // Filter nach Zahlungsart und Kategorie
    List<Auszahlung> findByZahlungsartAndVerwendungszweck(
            Auszahlung.Zahlungsart zahlungsart,
            Auszahlung.Verwendungszweck verwendungszweck,
            Sort sort
    );

    // Filter nach Zahlungsart und Datum
    List<Auszahlung> findByZahlungsartAndDatum(
            Auszahlung.Zahlungsart zahlungsart,
            LocalDate datum,
            Sort sort
    );

    // Filter nach Zahlungsart, Kategorie und Datum (neu hinzugefügt)
    List<Auszahlung> findByZahlungsartAndVerwendungszweckAndDatum(
            Auszahlung.Zahlungsart zahlungsart,
            Auszahlung.Verwendungszweck verwendungszweck,
            LocalDate datum,
            Sort sort
    );
}

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

    List<Auszahlung> findByDatum(
            LocalDate datum,
            Sort sort
    );

    List<Auszahlung> findByVerwendungszweck(
            Auszahlung.Verwendungszweck verwendungszweck,
            Sort sort
    );

    List<Auszahlung> findByVerwendungszweckAndDatum(
            Auszahlung.Verwendungszweck verwendungszweck,
            LocalDate datum,
            Sort sort
    );

    List<Auszahlung> findByNameContainingIgnoreCase(
            String name,
            Sort sort
    );

    List<Auszahlung> findByNameContainingIgnoreCaseAndDatum(
            String name,
            LocalDate datum,
            Sort sort
    );

    @Query("""
        SELECT a FROM Auszahlung a
        WHERE (:name IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%')))
          AND (:datum IS NULL OR a.datum = :datum)
          AND (:kategorie IS NULL OR a.verwendungszweck = :kategorie)
        ORDER BY a.datum DESC
    """)
    List<Auszahlung> filter(
            @Param("name") String name,
            @Param("datum") LocalDate datum,
            @Param("kategorie") Auszahlung.Verwendungszweck kategorie
    );

    @Query("""
        SELECT COALESCE(SUM(a.betrag), 0)
        FROM Auszahlung a
        WHERE a.datum = :datum
    """)
    BigDecimal summeAmTag(
            @Param("datum") LocalDate datum
    );

    @Query("""
        SELECT COALESCE(SUM(a.betrag), 0)
        FROM Auszahlung a
        WHERE a.datum >= :start AND a.datum < :ende
    """)
    BigDecimal summeProMonat(
            @Param("start") LocalDate start,
            @Param("ende") LocalDate ende
    );

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

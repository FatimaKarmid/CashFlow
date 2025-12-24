package com.cashflow.cashflow;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import java.util.List;

public interface AuszahlungRepository extends JpaRepository<Auszahlung, UUID> {

    List<Auszahlung> findByDatum(LocalDate datum);

    @Query("SELECT COALESCE(SUM(a.betrag), 0) FROM Auszahlung a WHERE a.datum = :datum")
    BigDecimal summeAmTag(@Param("datum") LocalDate datum);

    List<Auszahlung> findByVerwendungszweck(Auszahlung.Verwendungszweck verwendungszweck);

    @Query("""
SELECT COALESCE(SUM(a.betrag),0)
FROM Auszahlung a
WHERE MONTH(a.datum) = :monat AND YEAR(a.datum) = :jahr
""")
    BigDecimal summeProMonat(@Param("monat") int monat, @Param("jahr") int jahr);

    @Query("""
SELECT a.verwendungszweck, SUM(a.betrag)
FROM Auszahlung a
WHERE MONTH(a.datum) = :monat AND YEAR(a.datum) = :jahr
GROUP BY a.verwendungszweck
""")
    List<Object[]> summeNachKategorie(@Param("monat") int monat, @Param("jahr") int jahr);

}

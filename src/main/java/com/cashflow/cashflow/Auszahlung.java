package com.cashflow.cashflow;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;

@Entity
public class Auszahlung {

    @Id // Primärschlüssel
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private BigDecimal betrag;

    @Column(nullable = false)
    private LocalDate datum;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Zahlungsart zahlungsart;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Verwendungszweck verwendungszweck;

    private String notiz;

    // Setter für UUID id
    public void setId(UUID id) {
        this.id = id;
    }

    public enum Zahlungsart {
        BAR, KARTE, UEBERWEISUNG, LASTSCHRIFT, SONSTIGES
    }

    public enum Verwendungszweck {
        LEBENSMITTEL, KLEIDUNG, FAHRTKOSTEN, MIETE, FREIZEIT, GESUNDHEIT, SONSTIGES
    }

    public Auszahlung() {
    }

    public Auszahlung(BigDecimal betrag,
                      LocalDate datum,
                      Zahlungsart zahlungsart,
                      Verwendungszweck verwendungszweck,
                      String notiz) {
        this.betrag = betrag;
        this.datum = datum;
        this.zahlungsart = zahlungsart;
        this.verwendungszweck = verwendungszweck;
        this.notiz = notiz;
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getBetrag() {
        return betrag;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public Zahlungsart getZahlungsart() {
        return zahlungsart;
    }

    public Verwendungszweck getVerwendungszweck() {
        return verwendungszweck;
    }

    public String getNotiz() {
        return notiz;
    }

    public boolean istAmTag(LocalDate tag) {
        return datum.equals(tag);
    }

    public void setBetrag(BigDecimal betrag) {
        this.betrag = betrag;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public void setZahlungsart(Zahlungsart zahlungsart) {
        this.zahlungsart = zahlungsart;
    }

    public void setVerwendungszweck(Verwendungszweck verwendungszweck) {
        this.verwendungszweck = verwendungszweck;
    }

    public void setNotiz(String notiz) {
        this.notiz = notiz;
    }
}

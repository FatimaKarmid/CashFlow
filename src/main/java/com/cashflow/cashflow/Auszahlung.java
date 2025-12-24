package com.cashflow.cashflow;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Auszahlung {

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    @Positive
    @Column(nullable = false)
    private BigDecimal betrag;

    @NotNull
    @Column(nullable = false)
    private LocalDate datum;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Zahlungsart zahlungsart;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Verwendungszweck verwendungszweck;

    private String notiz;

    // ===== ENUMS =====

    public enum Zahlungsart {
        BAR, KARTE, UEBERWEISUNG, LASTSCHRIFT, SONSTIGES
    }

    public enum Verwendungszweck {
        LEBENSMITTEL,
        KLEIDUNG,
        FAHRTKOSTEN,
        MIETE,
        FREIZEIT,
        GESUNDHEIT,
        SONSTIGES
    }

    // ===== GETTER / SETTER =====

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getBetrag() {
        return betrag;
    }

    public void setBetrag(BigDecimal betrag) {
        this.betrag = betrag;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public Zahlungsart getZahlungsart() {
        return zahlungsart;
    }

    public void setZahlungsart(Zahlungsart zahlungsart) {
        this.zahlungsart = zahlungsart;
    }

    public Verwendungszweck getVerwendungszweck() {
        return verwendungszweck;
    }

    public void setVerwendungszweck(Verwendungszweck verwendungszweck) {
        this.verwendungszweck = verwendungszweck;
    }

    public String getNotiz() {
        return notiz;
    }

    public void setNotiz(String notiz) {
        this.notiz = notiz;
    }
}

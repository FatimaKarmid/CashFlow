package com.cashflow.cashflow;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

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

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private Zahlungsart zahlungsart;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private Verwendungszweck verwendungszweck;

    private String notiz;

    // =========================
    // ENUMS
    // =========================

    public enum Zahlungsart {
        BAR, KARTE, UEBERWEISUNG, LASTSCHRIFT, SONSTIGES
    }

    public enum Verwendungszweck {
        LEBENSMITTEL(1),
        KLEIDUNG(2),
        FAHRTKOSTEN(3),
        MIETE(4),
        FREIZEIT(5),
        GESUNDHEIT(6),
        SONSTIGES(7);

        private final int value;

        Verwendungszweck(int value) {
            this.value = value;
        }

        @JsonValue
        public int getValue() {
            return value;
        }

        @JsonCreator
        public static Verwendungszweck fromString(String value) {
            return Verwendungszweck.valueOf(value.toUpperCase());
        }

        public static Verwendungszweck fromInt(int i) {
            for (Verwendungszweck v : values()) {
                if (v.value == i) {
                    return v;
                }
            }
            throw new IllegalArgumentException("Unbekannter Wert: " + i);
        }
    }

    // =========================
    // KONSTRUKTOREN
    // =========================

    public Auszahlung() {
    }

    public Auszahlung(
            BigDecimal betrag,
            LocalDate datum,
            Zahlungsart zahlungsart,
            Verwendungszweck verwendungszweck,
            String notiz
    ) {
        this.betrag = betrag;
        this.datum = datum;
        this.zahlungsart = zahlungsart;
        this.verwendungszweck = verwendungszweck;
        this.notiz = notiz;
    }

    // =========================
    // GETTER / SETTER
    // =========================

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

    // =========================
    // HELFER
    // =========================

    public boolean istAmTag(LocalDate tag) {
        return datum.equals(tag);
    }
}

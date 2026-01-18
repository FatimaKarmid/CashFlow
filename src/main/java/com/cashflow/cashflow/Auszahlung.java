package com.cashflow.cashflow;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "auszahlung",
        indexes = {
                @Index(name = "idx_auszahlung_datum", columnList = "datum"),
                @Index(name = "idx_auszahlung_kategorie", columnList = "verwendungszweck")
        }
)
public class Auszahlung {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @NotNull
    @Positive
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal betrag;

    @NotNull
    @Column(nullable = false)
    private LocalDate datum;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Zahlungsart zahlungsart;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Verwendungszweck verwendungszweck;

    @Column(length = 255)
    private String notiz;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String name;


    public enum Zahlungsart {
        BAR,
        KARTE,
        UEBERWEISUNG,
        LASTSCHRIFT,
        SONSTIGES
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


    protected Auszahlung() {
        // JPA only
    }

    public Auszahlung(
            String name,
            BigDecimal betrag,
            LocalDate datum,
            Zahlungsart zahlungsart,
            Verwendungszweck verwendungszweck,
            String notiz
    ) {
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

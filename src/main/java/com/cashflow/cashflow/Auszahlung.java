package com.cashflow.cashflow;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

@Entity
public class Auszahlung {

    @Id // primärschlüssel
    private UUID id;
    private BigDecimal betrag;
    @Column(name = "datum")
    private LocalDate datum;
    private Zahlungsart zahlungsart;
    private Verwendungszweck verwendungszweck;
    private String notiz;

    public enum Zahlungsart {
        BAR, KARTE, UEBERWEISUNG, LASTSCHRIFT, SONSTIGES
    }

    public enum Verwendungszweck {
        LEBENSMITTEL, KLEIDUNG, FAHRTKOSTEN, MIETE, FREIZEIT, GESUNDHEIT, SONSTIGES
    }

    public Auszahlung() {
        this.id = null;
        this.betrag = null;
        this.datum = null;
        this.zahlungsart = null;
        this.verwendungszweck = null;
        this.notiz = null;
    }

    public Auszahlung(UUID id,
                      BigDecimal betrag,
                      LocalDate datum,
                      Zahlungsart zahlungsart,
                      Verwendungszweck verwendungszweck,
                      String notiz) {

        this.id = id;
        this.betrag = betrag;
        this.datum = datum;
        this.zahlungsart = zahlungsart;
        this.verwendungszweck = verwendungszweck;
        this.notiz = notiz;
    }


    public UUID getId() { return id; }

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
}
package com.cashflow.cashflow;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Auszahlung {

    @Id
    private final UUID id;

    private final BigDecimal betrag;
    private final LocalDate datum;
    private final Zahlungsart zahlungsart;
    private final Verwendungszweck verwendungszweck;
    private final String notiz;

    public enum Zahlungsart {
        BAR, KARTE, UEBERWEISUNG, LASTSCHRIFT, SONSTIGES
    }

    public enum Verwendungszweck {
        LEBENSMITTEL, KLEIDUNG, FAHRTKOSTEN, MIETE, FREIZEIT, GESUNDHEIT, SONSTIGES
    }

    public Auszahlung() {
        this.id = null; // Null setzen, da UUID final ist
        this.betrag = null; // Null setzen für BigDecimal
        this.datum = null; // Null für LocalDate
        this.zahlungsart = null; // Null für Enum
        this.verwendungszweck = null; // Null für Enum
        this.notiz = null; // Null für String
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
package com.cashflow.cashflow;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public final class Auszahlung {

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

    public Auszahlung(UUID id,
                      BigDecimal betrag,
                      LocalDate datum,
                      Zahlungsart zahlungsart,
                      Verwendungszweck verwendungszweck,
                      String notiz) {

        this.id = Objects.requireNonNull(id, "id darf nicht null sein");
        this.betrag = validateBetrag(betrag);
        this.datum = Objects.requireNonNull(datum, "datum darf nicht null sein");
        this.zahlungsart = Objects.requireNonNull(zahlungsart, "zahlungsart darf nicht null sein");
        this.verwendungszweck = Objects.requireNonNull(verwendungszweck, "verwendungszweck darf nicht null sein");
        this.notiz = validateNotiz(notiz);
    }

    private static BigDecimal validateBetrag(BigDecimal raw) {
        Objects.requireNonNull(raw, "betrag darf nicht null sein");
        BigDecimal normalized = raw.setScale(2, RoundingMode.HALF_UP);
        if (normalized.compareTo(new BigDecimal("0.01")) < 0) {
            throw new IllegalArgumentException("betrag muss >= 0.01 sein");
        }
        return normalized;
    }

    private static String validateNotiz(String notiz) {
        if (notiz == null || notiz.isBlank()) return null;
        String trimmed = notiz.trim();
        if (trimmed.length() > 255) {
            throw new IllegalArgumentException("notiz darf maximal 255 Zeichen haben");
        }
        return trimmed;
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
}
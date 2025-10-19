package de.htw.cashflow.backend.web;

public class TransactionController {
    private String title;
    private double amount;

    public TransactionController(String title, double amount) {
        this.title = title;
        this.amount = amount;
    }


    public String getTitle() { return title; }
    public double getAmount() { return amount; }
}

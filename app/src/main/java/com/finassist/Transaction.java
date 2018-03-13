package com.finassist;


import java.util.Date;

class Transaction {
    Account fromAcc, toAcc;
    Date dateTime;
    double amount;
    TransactionCategory category;
    String description;

    public Transaction(Account fromAcc, Account toAcc, Date dateTime, double amount, TransactionCategory category, String description) {
        this.fromAcc = fromAcc;
        this.toAcc = toAcc;
        this.dateTime = dateTime;
        this.amount = amount;
        this.category = category;
        this.description = description;
    }

    public Account getFromAcc() {
        return fromAcc;
    }

    public void setFromAcc(Account fromAcc) {
        this.fromAcc = fromAcc;
    }

    public Account getToAcc() {
        return toAcc;
    }

    public void setToAcc(Account toAcc) {
        this.toAcc = toAcc;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public TransactionCategory getCategory() {
        return category;
    }

    public void setCategory(TransactionCategory category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

package com.finassist.data;


import android.support.annotation.IntDef;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;

public class Transaction implements Serializable {
    public static final int TYPE_INCOME = 0;
    public static final int TYPE_EXPENDITURE = 1;
    public static final int TYPE_TRANSFER = 2;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            TYPE_INCOME,
            TYPE_EXPENDITURE,
            TYPE_TRANSFER
    })
    public @interface TypeDef {}

    String id;
    int type;
    Account fromAcc, toAcc;
    Date dateTime;
    double amount;
    TransactionCategory category;
    String description;

    public Transaction() {}

    public Transaction(@TypeDef int type, Account fromAcc, Account toAcc, Date dateTime, double amount, TransactionCategory category, String description) {
        this.type = type;
        this.fromAcc = fromAcc;
        this.toAcc = toAcc;
        this.dateTime = dateTime;
        this.amount = amount;
        this.category = category;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

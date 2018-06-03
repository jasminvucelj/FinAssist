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

    public static final Integer[] typeArray = {TYPE_INCOME, TYPE_EXPENDITURE, TYPE_TRANSFER};

    /*
    @Retention(RetentionPolicy.SOURCE)
    @IntDef(value = {TYPE_INCOME, TYPE_EXPENDITURE, TYPE_TRANSFER})
    public @interface TypeDef {}
    */

    private String id = null;
	private int type;
	private Account fromAcc, toAcc;
	private Date dateTime;
	private double amount;
	private TransactionCategory category;
	private String description;

    public Transaction() {}

    public Transaction(int type, Account fromAcc, Account toAcc, Date dateTime, double amount, TransactionCategory category, String description) {
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

    public int getType() {
    	return type;
	}

	public void setType(int type) {
    	this.type = type;
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

    public String transferAccountsToString() {
    	if(type != TYPE_TRANSFER) return "";
    	return fromAcc.getName() + " -> " + toAcc.getName();
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (type != that.type) return false;
        if (Double.compare(that.amount, amount) != 0) return false;
        if (!fromAcc.equals(that.fromAcc)) return false;
        if (!toAcc.equals(that.toAcc)) return false;
        if (!dateTime.equals(that.dateTime)) return false;
        if (!category.equals(that.category)) return false;
        return description != null ? description.equals(that.description) : that.description == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = type;
        result = 31 * result + fromAcc.hashCode();
        result = 31 * result + toAcc.hashCode();
        result = 31 * result + dateTime.hashCode();
        temp = Double.doubleToLongBits(amount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + category.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}

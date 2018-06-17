package com.finassist.data;


import java.io.Serializable;

public class Account implements Serializable {
    private String id = null;
    private String name;
    private String description;
    private double balance;

    public Account() {}

    public Account(String name, String description, double balance) {
        this.name = name;
        this.description = description;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return name;
    }

    public Account clone() {
    	Account newAccount = new Account();

    	newAccount.id = this.id;
		newAccount.name = this.name;
		newAccount.description = this.description;
		newAccount.balance = this.balance;

		return newAccount;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Account account = (Account) o;

		if (Double.compare(account.balance, balance) != 0) return false;
		if (id != null ? !id.equals(account.id) : account.id != null) return false;
		if (!name.equals(account.name)) return false;
		return description.equals(account.description);
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		result = name.hashCode();
		result = 31 * result + description.hashCode();
		temp = Double.doubleToLongBits(balance);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
}

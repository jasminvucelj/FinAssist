package com.finassist.data;


import java.io.Serializable;

public abstract class Account implements Serializable {
	public static final int TYPE_CASH_ACCOUNT = 0;
	public static final int TYPE_CURRENT_ACCOUNT = 1;
	public static final int TYPE_GIRO_ACCOUNT = 2;
	public static final int TYPE_SAVING_ACCOUNT = 3;
	public static final int TYPE_CREDIT_CARD = 4;
	public static final int TYPE_DEBIT_CARD = 5;

	public static final String[] accountTypeLabels = {"Cash account",
			"Current account",
			"Giro account",
			"Savings account",
			"Credit card",
			"Debit card"
	};


	String id = null;
	String name;
	int type;

    public Account() {}

    public Account(String name, int type) {
        this.name = name;
        this.type = type;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public abstract double getBalance();

    public abstract void setBalance(double balance);

	@Override
    public String toString() {
        return name;
    }

    public abstract void processTransaction(Transaction transaction);

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Account account = (Account) o;

		if (type != account.type) return false;
		if (!id.equals(account.id)) return false;
		return name.equals(account.name);
	}

	@Override
	public int hashCode() {
		int result = id.hashCode();
		result = 31 * result + name.hashCode();
		result = 31 * result + type;
		return result;
	}
}

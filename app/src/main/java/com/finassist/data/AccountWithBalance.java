package com.finassist.data;

public class AccountWithBalance extends Account {
	int type;
	double balance;

	public AccountWithBalance() {}

	public AccountWithBalance(String name, int type, double balance) {
		super(name, type);
		this.balance = balance;
	}

	@Override
	public AccountWithBalance clone() {
		AccountWithBalance newAccount = new AccountWithBalance();

		newAccount.id = this.id;
		newAccount.name = this.name;
		newAccount.type = this.type;
		newAccount.balance = this.balance;

		return newAccount;
	}

	@Override
	public double getBalance() {
		return balance;
	}

	@Override
	public void setBalance(double balance) {
		this.balance = balance;
	}

	@Override
	public void processTransaction(boolean isLoss, double balanceChange) {
		if(isLoss) balance -= balanceChange;
		else balance += balanceChange;
	}
}

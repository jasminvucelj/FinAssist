package com.finassist.data;

public class CashAccount extends Account {

	public CashAccount() {}

	public CashAccount(String name) {
		super(name, Account.TYPE_CASH_ACCOUNT);
	}

	@Override
	public double getBalance() {
		return 0;
	}

	@Override
	public void setBalance(double balance) {

	}

	@Override
	public Account clone() {
		Account newAccount = new CashAccount();

		newAccount.id = this.id;
		newAccount.name = this.name;
		newAccount.type = this.type;

		return newAccount;
	}

	@Override
	public void processTransaction(Transaction transaction) {}
}

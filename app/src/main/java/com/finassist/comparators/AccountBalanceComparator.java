package com.finassist.comparators;

import com.finassist.data.Account;

import java.util.Comparator;

public class AccountBalanceComparator implements Comparator<Account> {
	@Override
	public int compare(Account o1, Account o2) {
		Double balance1 = o1.getBalance();
		Double balance2 = o2.getBalance();

		return balance1.compareTo(balance2);
	}
}

package com.finassist.comparators;

import com.finassist.data.Account;

import java.util.Comparator;

public class AccountTypeComparator implements Comparator<Account> {
	@Override
	public int compare(Account o1, Account o2) {
		Integer type1 = o1.getType();
		Integer type2 = o2.getType();

		return type1.compareTo(type2);
	}
}

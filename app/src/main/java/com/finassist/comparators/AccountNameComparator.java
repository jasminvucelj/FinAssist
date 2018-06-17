package com.finassist.comparators;

import com.finassist.data.Account;

import java.util.Comparator;

public class AccountNameComparator implements Comparator<Account> {
	@Override
	public int compare(Account o1, Account o2) {
		String name1 = o1.getName();
		String name2 = o2.getName();

		return name1.compareToIgnoreCase(name2);
	}
}

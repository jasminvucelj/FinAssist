package com.finassist.comparators;

import com.finassist.data.Transaction;

import java.util.Comparator;

public class TransactionAmountComparator implements Comparator<Transaction> {
	@Override
	public int compare(Transaction o1, Transaction o2) {
		Double amount1 = o1.getAmount();
		Double amount2 = o2.getAmount();

		return amount1.compareTo(amount2);
	}
}

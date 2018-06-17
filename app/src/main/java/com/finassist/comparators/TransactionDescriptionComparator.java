package com.finassist.comparators;

import com.finassist.data.Transaction;

import java.util.Comparator;
import java.util.Date;

public class TransactionDescriptionComparator implements Comparator<Transaction> {
	@Override
	public int compare(Transaction o1, Transaction o2) {
		String description1 = o1.getDescription();
		String description2 = o2.getDescription();

		return description1.compareToIgnoreCase(description2);
	}
}

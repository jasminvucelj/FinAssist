package com.finassist.comparators;

import com.finassist.data.Transaction;

import java.util.Comparator;
import java.util.Date;

public class TransactionDateComparator implements Comparator<Transaction> {
	@Override
	public int compare(Transaction o1, Transaction o2) {
		Date date1 = o1.getDateTime();
		Date date2 = o2.getDateTime();

		return date1.compareTo(date2);
	}
}

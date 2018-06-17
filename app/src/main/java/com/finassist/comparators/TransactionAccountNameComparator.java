package com.finassist.comparators;

import com.finassist.data.Account;
import com.finassist.data.Transaction;

import java.util.Comparator;

public class TransactionAccountNameComparator implements Comparator<Transaction> {
	@Override
	public int compare(Transaction o1, Transaction o2) {
		// sorting by user account
		Account acc1 = getUserAccount(o1);
		Account acc2 = getUserAccount(o2);

		return new AccountNameComparator().compare(acc1, acc2);
	}

	private Account getUserAccount(Transaction transaction) {
		// -> toAcc if type is income
		// -> fromAcc if type is expenditure or transfer
		if (transaction.getType() != Transaction.TYPE_INCOME) {
			return transaction.getFromAcc();
		}
		return transaction.getToAcc();
	}
}

package com.finassist.helpers;

import com.finassist.data.Account;
import com.finassist.data.Transaction;

import java.util.Date;
import java.util.List;

import static com.finassist.helpers.ObjectListHelper.filterTransactionsByAnyAccount;

public class AccountBalanceHelper {
	public static Double[] getAccountBalanceStartEndDateValues(List<Transaction> transactionList, Account account, Date startDate, Date endDate) {
		double startValue;
		double endValue = account.getBalance();

		// get only transactions that involve the selected account
		List<Transaction> filteredTransactions = filterTransactionsByAnyAccount(transactionList, account);

		// first the transitions post the end date (endValue)
		for (Transaction transaction: filteredTransactions) {
			Date transactionDate = transaction.getDateTime();
			if (!endDate.after(transactionDate)) {
				endValue = modifyBalanceValue(transaction, account, endValue);
			}
		}

		startValue = endValue;

		// then the transitions between the start and the end date (startValue)
		for (Transaction transaction: filteredTransactions) {
			Date transactionDate = transaction.getDateTime();
			if (!startDate.after(transactionDate) && !endDate.before(transactionDate)) {
				startValue = modifyBalanceValue(transaction, account, startValue);
			}
		}

		return new Double[]{startValue, endValue};
	}

	private static double modifyBalanceValue(Transaction transaction, Account account, double initialValue) {
		// if the account received money, subtract it
		if(transaction.getType() == Transaction.TYPE_INCOME
				|| (transaction.getType() == Transaction.TYPE_TRANSFER
				&& transaction.getToAcc().equals(account))) {
			return initialValue - transaction.getAmount();
		}
		// if the account spent money, add it
		else if(transaction.getType() == Transaction.TYPE_EXPENDITURE ||
				(transaction.getType() == Transaction.TYPE_TRANSFER
						&& transaction.getFromAcc().equals(account))) {
			return initialValue + transaction.getAmount();
		}

		return initialValue; // shouldn't ever happen
	}
}

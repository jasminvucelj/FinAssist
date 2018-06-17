package com.finassist.helpers;

import com.finassist.comparators.TransactionAccountNameComparator;
import com.finassist.comparators.TransactionAmountComparator;
import com.finassist.comparators.TransactionDateComparator;
import com.finassist.comparators.TransactionDescriptionComparator;
import com.finassist.data.Account;
import com.finassist.data.Transaction;
import com.finassist.data.TransactionCategory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.finassist.helpers.DateHelper.getFirstDayOfEarlierMonth;
import static com.finassist.helpers.DateHelper.getFirstDayOfMonth;
import static com.finassist.helpers.DateHelper.getFirstDayOfPreviousMonth;
import static com.finassist.helpers.DateHelper.getFirstDayOfWeek;
import static com.finassist.helpers.DateHelper.getFirstDayOfYear;
import static com.finassist.helpers.DateHelper.getLastDayOfPreviousMonth;
import static com.finassist.helpers.DateHelper.getStartOfDay;
import static com.finassist.helpers.DateHelper.getYesterday;

public class ObjectListHelper {

	//region SORT

	public static void sortTransactionsByAccountName(
			List<Transaction> transactions, boolean ascending) {
		Collections.sort(transactions, new TransactionAccountNameComparator());
		if (!ascending) {
			Collections.reverse(transactions);
		}
	}

	public static void sortTransactionsByAccountName(List<Transaction> transactions) {
		sortTransactionsByAccountName(transactions, true);
	}

	public static void sortTransactionsByAmount(List<Transaction> transactions, boolean ascending) {
		Collections.sort(transactions, new TransactionAmountComparator());
		if (!ascending) {
			Collections.reverse(transactions);
		}
	}

	public static void sortTransactionsByAmount(List<Transaction> transactions) {
		sortTransactionsByAmount(transactions, true);
	}

	public static void sortTransactionsByDate(List<Transaction> transactions, boolean ascending) {
		Collections.sort(transactions, new TransactionDateComparator());
		if (!ascending) {
			Collections.reverse(transactions);
		}
	}

	public static void sortTransactionsByDate(List<Transaction> transactions) {
		sortTransactionsByDate(transactions, true);
	}

	public static void sortTransactionsByDescription(
			List<Transaction> transactions, boolean ascending) {
		Collections.sort(transactions, new TransactionDescriptionComparator());
		if (!ascending) {
			Collections.reverse(transactions);
		}
	}

	public static void sortTransactionsByDescription(List<Transaction> transactions) {
		sortTransactionsByDescription(transactions, true);
	}

	//endregion

	//region FILTER

	public static List<Transaction> filterTransactionsByStartEndDate(
			List<Transaction> transactions, Date startDate, Date endDate) {
		List<Transaction> filteredList = new ArrayList<>();

		for (Transaction transaction : transactions) {
			Date transactionDate = transaction.getDateTime();
			if (!startDate.after(transactionDate) && !endDate.before(transactionDate)) {
				filteredList.add(transaction);
			}
		}

		return filteredList;
	}

	public static List<Transaction> filterTransactionsByStartDate(
			List<Transaction> transactions, Date startDate) {
		return filterTransactionsByStartEndDate(
				transactions, startDate, Calendar.getInstance().getTime());
	}

	public static List<Transaction> filterTransactionsByAmount(
			List<Transaction> transactions, double amountMin, double amountMax) {
		List<Transaction> filteredList = new ArrayList<>();

		for (Transaction transaction : transactions) {
			double transactionAmount = transaction.getAmount();
			if (transactionAmount >= amountMin && transactionAmount <= amountMax) {
				filteredList.add(transaction);
			}
		}

		return filteredList;
	}

	public static List<Transaction> filterTransactionsByMinAmount(
			List<Transaction> transactions, double amountMin) {
		return filterTransactionsByAmount(transactions, amountMin, Double.MAX_VALUE);
	}

	public static List<Transaction> filterTransactionsByMaxAmount(
			List<Transaction> transactions, double amountMax) {
		return filterTransactionsByAmount(transactions, Double.MIN_VALUE, amountMax);
	}

	public static List<Transaction> filterTransactionsByType(
			List<Transaction> transactions, int type) {
		List<Transaction> filteredList = new ArrayList<>();

		for (Transaction transaction : transactions) {
			double transactionType = transaction.getType();
			if (transactionType == type) {
				filteredList.add(transaction);
			}
		}

		return filteredList;
	}

	public static List<Transaction> filterTransactionsByAnyAccount(
			List<Transaction> transactions, Account acc) {
		List<Transaction> filteredList = new ArrayList<>();

		for (Transaction transaction : transactions) {
			Account transactionToAcc = transaction.getToAcc();
			Account transactionFromAcc = transaction.getFromAcc();

			if (transactionToAcc.equals(acc) || transactionFromAcc.equals(acc)) {
				filteredList.add(transaction);
			}
		}

		return filteredList;
	}

	public static List<Transaction> filterTransactionsByAnyAccount(
			List<Transaction> transactions, Account fromAcc, Account toAcc) {
		List<Transaction> filteredList = new ArrayList<>();

		for (Transaction transaction : transactions) {
			Account transactionToAcc = transaction.getToAcc();
			Account transactionFromAcc = transaction.getFromAcc();

			if (transactionToAcc.equals(toAcc) || transactionFromAcc.equals(fromAcc)) {
				filteredList.add(transaction);
			}
		}

		return filteredList;
	}


	public static List<Transaction> filterTransactionsByFromAccount(
			List<Transaction> transactions, Account fromAcc) {
		List<Transaction> filteredList = new ArrayList<>();

		for (Transaction transaction : transactions) {
			Account transactionFromAcc = transaction.getFromAcc();
			if (transactionFromAcc.equals(fromAcc)) {
				filteredList.add(transaction);
			}
		}

		return filteredList;
	}

	public static List<Transaction> filterTransactionsByToAccount(
			List<Transaction> transactions, Account toAcc) {
		List<Transaction> filteredList = new ArrayList<>();

		for (Transaction transaction : transactions) {
			Account transactionToAcc = transaction.getToAcc();
			if (transactionToAcc.equals(toAcc)) {
				filteredList.add(transaction);
			}
		}

		return filteredList;
	}

	public static List<Transaction> filterTransactionsByCategory(
			List<Transaction> transactions, TransactionCategory category) {
		List<Transaction> filteredList = new ArrayList<>();

		for (Transaction transaction : transactions) {
			TransactionCategory transactionCategory = transaction.getCategory();
			if (transactionCategory.equals(category)) {
				filteredList.add(transaction);
			}
		}

		return filteredList;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////

	public static List<Transaction> filterTransactionsByToday(List<Transaction> transactions) {
		return filterTransactionsByStartDate(
				transactions, getStartOfDay(Calendar.getInstance().getTime()));
	}

	public static List<Transaction> filterTransactionsByYesterday(List<Transaction> transactions) {
		Date date = Calendar.getInstance().getTime();
		return filterTransactionsByStartEndDate(
				transactions, getStartOfDay(date), getYesterday(date));
	}

	public static List<Transaction> filterTransactionsByCurrentWeek(List<Transaction> transactions) {
		return filterTransactionsByStartDate(
				transactions, getFirstDayOfWeek(Calendar.getInstance().getTime()));
	}

	public static List<Transaction> filterTransactionsByLastXMonths(List<Transaction> transactions, int x) {
		return filterTransactionsByStartDate(
				transactions, getFirstDayOfEarlierMonth(Calendar.getInstance().getTime(), x));
	}

	public static List<Transaction> filterTransactionsByCurrentMonth(
			List<Transaction> transactions) {
		return filterTransactionsByStartDate(
				transactions, getFirstDayOfMonth(Calendar.getInstance().getTime()));
	}

	public static List<Transaction> filterTransactionsByPreviousMonth(
			List<Transaction> transactions) {
		Date date = Calendar.getInstance().getTime();
		return filterTransactionsByStartEndDate(
				transactions, getFirstDayOfPreviousMonth(date), getLastDayOfPreviousMonth(date));
	}

	public static List<Transaction> filterTransactionsByCurrentYear(
			List<Transaction> transactions) {
		return filterTransactionsByStartDate(
				transactions, getFirstDayOfYear(Calendar.getInstance().getTime()));
	}

	//endregion
}
package com.finassist.mock;

import com.finassist.data.Account;
import com.finassist.data.Transaction;
import com.finassist.data.TransactionCategory;
import com.finassist.helpers.FirebaseDatabaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Mocker {

    public static void generateMockUserData(String currentUserId) {

        Account account0 = new Account("DUMMY_EXPENDITURE_ACC", "", 0);
        Account account1 = new Account("Visa Electron", "Zaba žiro račun",7000);
        Account account2 = new Account("Maestro", "", 2500);

        SimpleDateFormat sdf = new SimpleDateFormat(
                "dd.MM.yyyy. HH:mm:ss",
                Locale.getDefault());

        Date date1 = null, date2 = null, date3 = null, date4 = null;
        try {
            date1 = sdf.parse("23.12.2017. 13:34:55");
            date2 = sdf.parse("27.12.2017. 17:23:28");
            date3 = sdf.parse("03.01.2018. 09:45:03");
            date4 = sdf.parse("12.01.2018. 20:01:49");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TransactionCategory tc1 = new TransactionCategory("Food");
        TransactionCategory tc2 = new TransactionCategory("Groceries", tc1);
        TransactionCategory tc3 = new TransactionCategory("Car");
        TransactionCategory tc4 = new TransactionCategory("Repairs", tc3);
        TransactionCategory tc5 = new TransactionCategory("Kids");
        TransactionCategory tc6 = new TransactionCategory("School", tc5);
        TransactionCategory tc7 = new TransactionCategory("Transfer");

        Transaction transaction0 = new Transaction(Transaction.TYPE_EXPENDITURE, account2, account0, date1, 253.67, tc2, "Monthly shopping");
        Transaction transaction1 = new Transaction(Transaction.TYPE_EXPENDITURE, account1, account0, date2, 1200, tc4, "Car repairs");
        Transaction transaction2 = new Transaction(Transaction.TYPE_EXPENDITURE, account1, account0, date3, 300, tc6, "Tennis training");
        Transaction transaction3 = new Transaction(Transaction.TYPE_TRANSFER, account2, account1, date4, 500, tc7, "Transfer");

        FirebaseDatabaseHelper.saveDummyAccount(account0, currentUserId);

        FirebaseDatabaseHelper.saveAccount(account1, currentUserId);
        FirebaseDatabaseHelper.saveAccount(account2, currentUserId);

        FirebaseDatabaseHelper.saveTransactionCategory(tc2, currentUserId);
        FirebaseDatabaseHelper.saveTransactionCategory(tc4, currentUserId);
        FirebaseDatabaseHelper.saveTransactionCategory(tc6, currentUserId);
        FirebaseDatabaseHelper.saveTransactionCategory(tc7, currentUserId);

        FirebaseDatabaseHelper.saveTransaction(transaction0, currentUserId);
        FirebaseDatabaseHelper.saveTransaction(transaction1, currentUserId);
        FirebaseDatabaseHelper.saveTransaction(transaction2, currentUserId);
        FirebaseDatabaseHelper.saveTransaction(transaction3, currentUserId);

    }

}

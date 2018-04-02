package com.finassist.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.finassist.R;
import com.finassist.classes.Account;
import com.finassist.classes.Transaction;
import com.finassist.classes.TransactionCategory;
import com.finassist.controllers.FirebaseDatabaseController;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {

    private TextView tv;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        tv = findViewById(R.id.tvTest);
        RecyclerView rvTransactions = findViewById(R.id.rv_transactions);
        rvTransactions.setHasFixedSize(true);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        try {
            currentUserId = FirebaseDatabaseController.getCustomUserId(currentUser);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        tv.setText("Welcome! Your unique ID is " + currentUserId + ".");

        generateMockUserData();

        // TODO fetch user transaction data from Firebase
        // TODO recyclerView progressBar
        // TODO recyclerView layoutManager
        // TODO recyclerView click -> editTransaction
        // TODO recyclerView swipe L/R to delete
        // TODO toolbar edit accounts, options


    }

    private void generateMockUserData() {

        Account account0 = new Account("DUMMY_EXPENDITURE_ACC", "");
        Account account1 = new Account("Visa Electron", "Zaba žiro račun");
        Account account2 = new Account("Gotovina", "");

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

        FirebaseDatabaseController.saveDummyAccountToDatabase(account0, currentUserId);

        FirebaseDatabaseController.saveAccountToDatabase(account1, currentUserId);
        FirebaseDatabaseController.saveAccountToDatabase(account2, currentUserId);

        FirebaseDatabaseController.saveTransactionCategoryToDatabase(tc2, currentUserId);
        FirebaseDatabaseController.saveTransactionCategoryToDatabase(tc4, currentUserId);
        FirebaseDatabaseController.saveTransactionCategoryToDatabase(tc6, currentUserId);
        FirebaseDatabaseController.saveTransactionCategoryToDatabase(tc7, currentUserId);

        FirebaseDatabaseController.saveTransactionToDatabase(transaction0, currentUserId);
        FirebaseDatabaseController.saveTransactionToDatabase(transaction1, currentUserId);
        FirebaseDatabaseController.saveTransactionToDatabase(transaction2, currentUserId);
        FirebaseDatabaseController.saveTransactionToDatabase(transaction3, currentUserId);

    }

}

package com.finassist.controllers;

import com.finassist.classes.Account;
import com.finassist.classes.Transaction;
import com.finassist.classes.TransactionCategory;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FirebaseDatabaseController {
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference dbAccounts = database.getReference("accounts");
    private static DatabaseReference dbDummyAccounts = database.getReference("dummy_accounts");
    private static DatabaseReference dbTransactionCategories = database.getReference("transaction_categories");
    private static DatabaseReference dbTransactions = database.getReference("transactions");


    public static String getCustomUserId(FirebaseUser user) throws NullPointerException {
        return (user.getUid() + "_" + user.getEmail()).replaceAll("[^A-Za-z0-9]", "_");
    }


    public static void saveAccountToDatabase(Account account, String userId) {
        String objectId = dbAccounts.child(userId).push().getKey();
        account.setId(objectId);
        dbAccounts.child(userId).child(objectId).setValue(account);
    }


    public static void saveDummyAccountToDatabase(Account account, String userId) {
        String objectId = dbDummyAccounts.child(userId).push().getKey();
        account.setId(objectId);
        dbDummyAccounts.child(userId).child(objectId).setValue(account);
    }


    public static void saveTransactionCategoryToDatabase(TransactionCategory transactionCategory, String userId) {
        String objectId = dbTransactionCategories.child(userId).push().getKey();
        transactionCategory.setId(objectId);
        dbTransactionCategories.child(userId).child(objectId).setValue(transactionCategory);
    }


    public static void saveTransactionToDatabase(Transaction transaction, String userId) {
        String objectId = dbTransactions.child(userId).push().getKey();
        transaction.setId(objectId);
        dbTransactions.child(userId).child(objectId).setValue(transaction);
    }
}

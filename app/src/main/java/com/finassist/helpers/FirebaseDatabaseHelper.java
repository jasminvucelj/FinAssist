package com.finassist.helpers;

import com.finassist.data.Account;
import com.finassist.data.Transaction;
import com.finassist.data.TransactionCategory;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FirebaseDatabaseHelper {
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final DatabaseReference dbAccounts = database.getReference("accounts");
    private static final DatabaseReference dbDummyAccounts = database.getReference("dummy_accounts");
    private static final DatabaseReference dbTransactionCategories = database.getReference("transaction_categories");
    private static final DatabaseReference dbTransactions = database.getReference("transactions");


    public static String getCustomUserId(FirebaseUser user) throws NullPointerException {
        return (user.getUid() + "_" + user.getEmail()).replaceAll("[^A-Za-z0-9]", "_");
    }

    //region SAVE

    public static void saveTransaction(Transaction transaction, String userId) {
        String objectId = dbTransactions.child(userId).push().getKey();
        transaction.setId(objectId);
        dbTransactions.child(userId).child(objectId).setValue(transaction);
    }


    public static void saveAccount(Account account, String userId) {
        String objectId = dbAccounts.child(userId).push().getKey();
        account.setId(objectId);
        dbAccounts.child(userId).child(objectId).setValue(account);
    }


    public static void saveDummyAccount(Account account, String userId) {
        String objectId = dbDummyAccounts.child(userId).push().getKey();
        account.setId(objectId);
        dbDummyAccounts.child(userId).child(objectId).setValue(account);
    }


    public static void saveTransactionCategory(TransactionCategory transactionCategory, String userId) {
        String objectId = dbTransactionCategories.child(userId).push().getKey();
        transactionCategory.setId(objectId);
        dbTransactionCategories.child(userId).child(objectId).setValue(transactionCategory);
    }

    //endregion
}

package com.finassist.helpers;

import com.finassist.data.Account;
import com.finassist.data.Transaction;
import com.finassist.data.TransactionCategory;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FirebaseDatabaseHelper {
    public static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static final DatabaseReference dbAccounts = database.getReference("accounts");
    public static final DatabaseReference dbDummyAccounts = database.getReference("dummy_accounts");
    public static final DatabaseReference dbTransactionCategories = database.getReference("transaction_categories");
    public static final DatabaseReference dbTransactions = database.getReference("transactions");


    public static String getCustomUserId(FirebaseUser user) throws NullPointerException {
        return (user.getUid() + "_" + user.getEmail()).replaceAll("[^A-Za-z0-9]", "_");
    }

    //region SAVE

    public static void saveTransaction(Transaction transaction, String userId) {
        String objectId = dbTransactions.child(userId).push().getKey();
        transaction.setId(objectId);
        dbTransactions.child(userId).child(objectId).setValue(transaction);
    }


	public static void saveTransaction(Transaction transaction, String userId, String objectId) {
		dbTransactions.child(userId).child(objectId).setValue(transaction);
	}


    public static void saveAccount(Account account, String userId) {
        String objectId = dbAccounts.child(userId).push().getKey();
        account.setId(objectId);
        dbAccounts.child(userId).child(objectId).setValue(account);
    }


	public static void saveAccount(Account account, String userId, String objectId) {
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


	public static void saveTransactionCategory(TransactionCategory transactionCategory,
											   String userId,
											   String objectId) {
		dbTransactionCategories.child(userId).child(objectId).setValue(transactionCategory);
	}

    //endregion

	// region DELETE

	public static void deleteTransaction(Transaction transaction, String userId) {
		dbTransactions.child(userId).child(transaction.getId()).removeValue(); // TODO change account balances!
	}

	public static void deleteAccount(Account account, String userId) { // TODO also delete transactions!
		dbAccounts.child(userId).child(account.getId()).removeValue();
	}

	// endregion

}

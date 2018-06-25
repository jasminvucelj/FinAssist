package com.finassist.helpers;

import com.finassist.data.Account;
import com.finassist.data.AccountWithBalance;
import com.finassist.data.CashAccount;
import com.finassist.data.Transaction;
import com.finassist.data.TransactionCategory;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class FirebaseDatabaseHelper {
    public static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static final DatabaseReference dbAccounts = database.getReference("accounts");
    public static final DatabaseReference dbDummyAccounts = database.getReference("dummy_accounts");
    public static final DatabaseReference dbTransactionCategories = database.getReference("transaction_categories");
    public static final DatabaseReference dbTransactions = database.getReference("transactions");

    public static List<TransactionCategory> transactionCategories = new ArrayList<>();

    public static Account dummyAccount;

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


    public static void addDummyAccount(String userId) {
    	dummyAccount = new CashAccount("DUMMY_EXPENDITURE_ACC");
		String objectId = dbDummyAccounts.child(userId).push().getKey();
		dummyAccount.setId(objectId);
        dbDummyAccounts.child(userId).child(objectId).setValue(dummyAccount);
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

	// region HELPER
	public static void initTransactionCategories() {
		transactionCategories.add(new TransactionCategory("Transfer")); // 0
		transactionCategories.add(new TransactionCategory("Withdrawal"));

		TransactionCategory tcIncome = new TransactionCategory("Income"); // 2
		transactionCategories.add(tcIncome);
		transactionCategories.add(new TransactionCategory("Salary", tcIncome));
		transactionCategories.add(new TransactionCategory("Bonus/Tips", tcIncome));
		transactionCategories.add(new TransactionCategory("Selling", tcIncome));
		transactionCategories.add(new TransactionCategory("Gifts", tcIncome));
		transactionCategories.add(new TransactionCategory("Scholarship", tcIncome));
		transactionCategories.add(new TransactionCategory("Investments", tcIncome));
		transactionCategories.add(new TransactionCategory("Tax refunds", tcIncome));
		transactionCategories.add(new TransactionCategory("Debt repayments", tcIncome));
		transactionCategories.add(new TransactionCategory("Other", tcIncome));

		TransactionCategory tcBusiness = new TransactionCategory("Business"); // 12
		transactionCategories.add(tcBusiness);
		transactionCategories.add(new TransactionCategory("Insurance", tcBusiness));
		transactionCategories.add(new TransactionCategory("Investments", tcBusiness));
		transactionCategories.add(new TransactionCategory("Equipment", tcBusiness));
		transactionCategories.add(new TransactionCategory("Education/training", tcBusiness));
		transactionCategories.add(new TransactionCategory("Wages", tcBusiness));
		transactionCategories.add(new TransactionCategory("Taxes/fees", tcBusiness));
		transactionCategories.add(new TransactionCategory("Other", tcBusiness));

		TransactionCategory tcEducation = new TransactionCategory("Education"); // 20
		transactionCategories.add(tcEducation);
		transactionCategories.add(new TransactionCategory("Tuition", tcEducation));
		transactionCategories.add(new TransactionCategory("Learning material", tcEducation));
		transactionCategories.add(new TransactionCategory("Supplies", tcEducation));
		transactionCategories.add(new TransactionCategory("Field trip", tcEducation));
		transactionCategories.add(new TransactionCategory("Fees", tcEducation));
		transactionCategories.add(new TransactionCategory("Student loans", tcEducation));
		transactionCategories.add(new TransactionCategory("Other", tcEducation));

		TransactionCategory tcEvents = new TransactionCategory("Events"); // 28
		transactionCategories.add(tcEvents);
		transactionCategories.add(new TransactionCategory("Wedding", tcEvents));
		transactionCategories.add(new TransactionCategory("Moving", tcEvents));
		transactionCategories.add(new TransactionCategory("Birthday", tcEvents));
		transactionCategories.add(new TransactionCategory("Anniversary", tcEvents));
		transactionCategories.add(new TransactionCategory("Holiday", tcEvents));
		transactionCategories.add(new TransactionCategory("Other", tcEvents));

		TransactionCategory tcFood = new TransactionCategory("Food"); // 35
		transactionCategories.add(tcFood);
		transactionCategories.add(new TransactionCategory("Groceries", tcFood));
		transactionCategories.add(new TransactionCategory("Snacks", tcFood));
		transactionCategories.add(new TransactionCategory("Drinks", tcFood));
		transactionCategories.add(new TransactionCategory("Dining out", tcFood));
		transactionCategories.add(new TransactionCategory("Coffee house", tcFood));
		transactionCategories.add(new TransactionCategory("Other", tcFood));

		TransactionCategory tcHealthcare = new TransactionCategory("Healthcare"); // 42
		transactionCategories.add(tcHealthcare);
		transactionCategories.add(new TransactionCategory("Health insurance", tcHealthcare)); // 37
		transactionCategories.add(new TransactionCategory("Life insurance", tcHealthcare));
		transactionCategories.add(new TransactionCategory("Appointments", tcHealthcare));
		transactionCategories.add(new TransactionCategory("Hospital stays", tcHealthcare));
		transactionCategories.add(new TransactionCategory("Medication", tcHealthcare));
		transactionCategories.add(new TransactionCategory("Other", tcHealthcare));

		TransactionCategory tcHome = new TransactionCategory("Household"); // 49
		transactionCategories.add(tcHome);
		transactionCategories.add(new TransactionCategory("Rent/mortgage", tcHome));
		transactionCategories.add(new TransactionCategory("Family", tcHome));
		transactionCategories.add(new TransactionCategory("Home maintenance", tcHome));
		transactionCategories.add(new TransactionCategory("Home improvement", tcHome));
		transactionCategories.add(new TransactionCategory("Supplies", tcHome));
		transactionCategories.add(new TransactionCategory("Other", tcHome));

		TransactionCategory tcLeisure = new TransactionCategory("Leisure"); // 56
		transactionCategories.add(tcLeisure);
		transactionCategories.add(new TransactionCategory("Literature", tcLeisure));
		transactionCategories.add(new TransactionCategory("Movies/TV/video", tcLeisure));
		transactionCategories.add(new TransactionCategory("Video games", tcLeisure));
		transactionCategories.add(new TransactionCategory("Sport/exercise", tcLeisure));
		transactionCategories.add(new TransactionCategory("Sporting events", tcLeisure));
		transactionCategories.add(new TransactionCategory("Cultural events", tcLeisure));
		transactionCategories.add(new TransactionCategory("Tourist attractions", tcLeisure));
		transactionCategories.add(new TransactionCategory("Travel", tcLeisure));
		transactionCategories.add(new TransactionCategory("Other", tcLeisure));

		TransactionCategory tcPets = new TransactionCategory("Pets"); // 66
		transactionCategories.add(tcPets);
		transactionCategories.add(new TransactionCategory("Food", tcPets));
		transactionCategories.add(new TransactionCategory("Supplies", tcPets));
		transactionCategories.add(new TransactionCategory("Veterinarian", tcPets));
		transactionCategories.add(new TransactionCategory("Other", tcPets));

		TransactionCategory tcSavings = new TransactionCategory("Savings"); // 71
		transactionCategories.add(tcSavings);
		transactionCategories.add(new TransactionCategory("Retirement", tcSavings));
		transactionCategories.add(new TransactionCategory("Investments", tcSavings));
		transactionCategories.add(new TransactionCategory("Reserve/emergency", tcSavings));
		transactionCategories.add(new TransactionCategory("Other", tcSavings));

		transactionCategories.add(new TransactionCategory("Taxes")); // 76

		TransactionCategory tcUtilities = new TransactionCategory("Utilities"); // 77
		transactionCategories.add(tcUtilities);
		transactionCategories.add(new TransactionCategory("Water", tcUtilities));
		transactionCategories.add(new TransactionCategory("Sewage", tcUtilities));
		transactionCategories.add(new TransactionCategory("Electricity", tcUtilities));
		transactionCategories.add(new TransactionCategory("Heating", tcUtilities));
		transactionCategories.add(new TransactionCategory("TV", tcUtilities));
		transactionCategories.add(new TransactionCategory("Phone", tcUtilities));
		transactionCategories.add(new TransactionCategory("Internet", tcUtilities));
		transactionCategories.add(new TransactionCategory("Garbage", tcUtilities));
		transactionCategories.add(new TransactionCategory("Other", tcUtilities));

		TransactionCategory tcVehicle = new TransactionCategory("Vehicle"); // 87
		transactionCategories.add(tcVehicle);
		transactionCategories.add(new TransactionCategory("Purchase", tcVehicle));
		transactionCategories.add(new TransactionCategory("Fuel", tcVehicle));
		transactionCategories.add(new TransactionCategory("Repairs/maintenance", tcVehicle));
		transactionCategories.add(new TransactionCategory("Registration", tcVehicle));
		transactionCategories.add(new TransactionCategory("Other", tcVehicle));

		transactionCategories.add(new TransactionCategory("Various")); // 93
	}

	public static void addTransactionCategories(String currentUserId) {
		for (TransactionCategory tc : transactionCategories) {
			FirebaseDatabaseHelper.saveTransactionCategory(tc, currentUserId);
		}
	}

	public static List<Transaction> readTransactions(DataSnapshot dataSnapshot) {
		List<Transaction> transactions = new ArrayList<>();
		try{
			for(DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

				String id;
				Account fromAcc, toAcc;
				double amount;
				Date dateTime;
				TransactionCategory category;
				String description;

				int fromAccType, toAccType;
				int transactionType = childSnapshot.child("type").getValue(Integer.class);

				if(transactionType == Transaction.TYPE_INCOME) {
					toAccType = childSnapshot.child("toAcc").child("type").getValue(Integer.class);
					if(toAccType == Account.TYPE_CASH_ACCOUNT) {
						toAcc = childSnapshot.child("toAcc").getValue(CashAccount.class);
					}
					else {
						toAcc = childSnapshot.child("toAcc").getValue(AccountWithBalance.class);
					}
					fromAcc = childSnapshot.child("fromAcc").getValue(CashAccount.class);
				}
				else if (transactionType == Transaction.TYPE_EXPENDITURE) {
					fromAccType = childSnapshot.child("fromAcc").child("type").getValue(Integer.class);
					if(fromAccType == Account.TYPE_CASH_ACCOUNT) {
						fromAcc = childSnapshot.child("fromAcc").getValue(CashAccount.class);
					}
					else {
						fromAcc = childSnapshot.child("fromAcc").getValue(AccountWithBalance.class);
					}
					toAcc = childSnapshot.child("toAcc").getValue(CashAccount.class);
				}
				else {
					fromAccType = childSnapshot.child("fromAcc").child("type").getValue(Integer.class);
					toAccType = childSnapshot.child("toAcc").child("type").getValue(Integer.class);

					if(fromAccType == Account.TYPE_CASH_ACCOUNT) {
						fromAcc = childSnapshot.child("fromAcc").getValue(CashAccount.class);
					}
					else {
						fromAcc = childSnapshot.child("fromAcc").getValue(AccountWithBalance.class);
					}
					if(toAccType == Account.TYPE_CASH_ACCOUNT) {
						toAcc = childSnapshot.child("toAcc").getValue(CashAccount.class);
					}
					else {
						toAcc = childSnapshot.child("toAcc").getValue(AccountWithBalance.class);
					}
				}

				id = childSnapshot.child("id").getValue(String.class);
				dateTime  = childSnapshot.child("dateTime").getValue(Date.class);
				amount = childSnapshot.child("amount").getValue(Double.class);
				description = childSnapshot.child("description").getValue(String.class);
				category = childSnapshot.child("category").getValue(TransactionCategory.class);

				Transaction tempTransaction = new Transaction(transactionType,
						fromAcc,
						toAcc,
						dateTime,
						amount,
						category,
						description);

				tempTransaction.setId(id);

				transactions.add(tempTransaction);
			}

			return transactions;

		} catch (NullPointerException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public static List<Account> readAccounts(DataSnapshot dataSnapshot) {
		try {
			List<Account> accounts = new ArrayList<>();
			for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
				int accountType = childSnapshot.child("type").getValue(Integer.class);

				if (accountType != Account.TYPE_CASH_ACCOUNT) {
					AccountWithBalance temp = childSnapshot.getValue(AccountWithBalance.class);
					accounts.add(temp);
				} else {
					CashAccount temp = childSnapshot.getValue(CashAccount.class);
					accounts.add(temp);
				}
			}

			return accounts;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public static List<Account> readAccountsWithBalance(DataSnapshot dataSnapshot) {
		try {
			List<Account> accounts = new ArrayList<>();
			for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
				int accountType = childSnapshot.child("type").getValue(Integer.class);

				if (accountType != Account.TYPE_CASH_ACCOUNT) {
					AccountWithBalance temp = childSnapshot.getValue(AccountWithBalance.class);
					accounts.add(temp);
				}
			}

			return accounts;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
	// endregion

}

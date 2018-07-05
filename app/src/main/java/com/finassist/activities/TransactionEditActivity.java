package com.finassist.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.finassist.R;
import com.finassist.data.Account;
import com.finassist.data.AccountWithBalance;
import com.finassist.data.CashAccount;
import com.finassist.data.Transaction;
import com.finassist.data.TransactionCategory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

import static com.finassist.helpers.FirebaseDatabaseHelper.dbAccounts;
import static com.finassist.helpers.FirebaseDatabaseHelper.dbDummyAccounts;
import static com.finassist.helpers.FirebaseDatabaseHelper.dbTransactionCategories;
import static com.finassist.helpers.FirebaseDatabaseHelper.readAccounts;
import static com.finassist.helpers.FirebaseDatabaseHelper.saveAccount;
import static com.finassist.helpers.FirebaseDatabaseHelper.saveTransaction;

public class TransactionEditActivity extends Activity {
	private String currentUserId;
    private int requestCode;
    private Transaction transaction;
    private Account dummyAccount, initialToAccount = null, initialFromAccount = null;
    private double initialAmount = 0;

    private ArrayAdapter<Account> accountAdapter;
	private ArrayAdapter<TransactionCategory> categoryAdapter;

    private List<Account> accountList;
    private List<TransactionCategory> transactionCategoryList;

    @BindView(R.id.toolbar)
	Toolbar toolbar;

	@BindView(R.id.tvFirstAccount)
	TextView tvFirstAccount;
	@BindView(R.id.spinnerFirstAccount)
	Spinner spinnerFirstAccount;
	@BindView(R.id.ibAccounts)
	ImageButton ibAddAccount;

	@BindView(R.id.tvSecondAccount)
	TextView tvSecondAccount;
	@BindView(R.id.llSecondAccount)
	LinearLayout llSecondAccount;
	@BindView(R.id.spinnerSecondAccount)
	Spinner spinnerSecondAccount;

	@BindView(R.id.spinnerCategory)
	Spinner spinnerCategory;
	@BindView(R.id.spinnerType)
	Spinner spinnerType;

	@BindView(R.id.etAmount)
	EditText etAmount;
	@BindView(R.id.etAccountDescription)
	EditText etDescription;

	@BindView(R.id.btnAccept)
	Button btnAccept;
	@BindView(R.id.btnCancel)
	Button btnCancel;

	// etAmount - when focus is lost, format number properly
	@OnFocusChange(R.id.etAmount)
	public void etAmount_onFocusChange(boolean hasFocus) {
		if(!hasFocus) {
			double amount = Double.parseDouble(etAmount.getText().toString());
			etAmount.setText(new DecimalFormat("#.00").format(amount));
		}
	}

	// ibAddAccount - starts the account overview activity.
	@OnClick(R.id.ibAccounts)
	public void ibAccounts_onClick() {
		Intent intent = new Intent(TransactionEditActivity.this, AccountOverviewActivity.class);
		startActivity(intent);
	}

	// btnAccept - edits the current transaction or saves a new one, as required, then returns to
	// the transaction overview activity.
	@OnClick(R.id.btnAccept)
	public void btnAccept_onClick() {
		if(requestCode == TransactionOverviewActivity.TRANSACTION_EDIT_REQUEST_CODE) { // edit existing
			queueSaveTransaction(transaction);
		}
		else { // add new
			queueSaveTransaction(new Transaction());
		}

		endActivity();
	}

	// btnCancel - returns to the transaction overview activity without changes.
	@OnClick(R.id.btnCancel)
	public void btnCancel_onClick() {
		endActivity();
	}

    @SuppressLint("ch.twint.walletapp.lint.debouncedOnClickListener")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_edit);

        accountList = new ArrayList<>();
        transactionCategoryList = new ArrayList<>();

		ButterKnife.bind(this);

		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// show the second account spinner only if transfer type is selected, hide it otherwise
		spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				int type = position;

				if (type == Transaction.TYPE_TRANSFER) {
					tvFirstAccount.setText(R.string.transaction_from_account);
					tvSecondAccount.setVisibility(View.VISIBLE);
					llSecondAccount.setVisibility(View.VISIBLE);
				}
				else {
					tvFirstAccount.setText(R.string.transaction_your_account);
					tvSecondAccount.setVisibility(View.GONE);
					llSecondAccount.setVisibility(View.GONE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

        Intent intent = getIntent();

        currentUserId = intent.getStringExtra("user_id");
		getDummyAccount();

        // get rq code - to find out if we're editing a transaction or creating a new one
        requestCode = intent.getIntExtra("request_code",
                TransactionOverviewActivity.TRANSACTION_CREATE_REQUEST_CODE);

		// if editing transaction, get it and display data from that transaction
        if (requestCode == TransactionOverviewActivity.TRANSACTION_EDIT_REQUEST_CODE) {
            transaction = (Transaction) intent.getSerializableExtra("transaction");
            initialToAccount = transaction.getToAcc();
			initialFromAccount = transaction.getFromAcc();
			initialAmount = transaction.getAmount();
			etAmount.setText(new DecimalFormat("#.00").format(initialAmount));
			etDescription.setText(transaction.getDescription());
        }

        setupSpinners();
    }

	/**
	 * Set the data for the new or edited transaction to be added to the database, and manipulates
	 * balances of accounts involved in the transaction.
	 * The algorithm is:
	 *
	 * 1. Calculate balance change (full transaction amount for a newly added transaction,
	 * difference between initial and new amount for edited transaction.
	 *
	 * 2. Check type of transaction, get corresponding type(s) of account(s) (only toAcc for income,
	 * only fromAcc for expenditure, both for transfer).
	 *
	 * 3. If account(s) got changed, cancel the transaction for the old one and apply it to the new
	 * one, else just apply the change.
	 *
	 * 4. Save altered account(s) to database.
	 *
	 * 5. Save transaction to database.
	 *
	 * @param activeTransaction the new or edited transaction.
	 */
	public void queueSaveTransaction(Transaction activeTransaction) {
		int type = spinnerType.getSelectedItemPosition();
		activeTransaction.setType(type);

		Account tempToAccount, tempFromAccount;

		double newAmount = Double.parseDouble(etAmount.getText().toString().trim());
		activeTransaction.setAmount(newAmount);

		double balanceChange = newAmount - initialAmount;

		if(type == Transaction.TYPE_INCOME) {
			tempToAccount = (Account) spinnerFirstAccount.getSelectedItem();

			if(tempToAccount.getType() == Account.TYPE_CASH_ACCOUNT) {
				tempToAccount = (CashAccount) spinnerFirstAccount.getSelectedItem();
				activeTransaction.setToAcc(tempToAccount);
			}
			else {
				tempToAccount = (AccountWithBalance) spinnerFirstAccount.getSelectedItem();
				activeTransaction.setToAcc(tempToAccount);
			}

			if(initialToAccount != null
					&& !initialToAccount.equals(dummyAccount)
					&& !tempToAccount.equals(initialToAccount)) {
				initialToAccount.processTransaction(Account.LOSS, initialAmount);
				saveAccount(initialToAccount, currentUserId, initialToAccount.getId());
			}

			if(!tempToAccount.equals(initialToAccount)) {
				tempToAccount.processTransaction(Account.GAIN, newAmount);
			}
			else {
				tempToAccount.processTransaction(Account.GAIN, balanceChange);
			}
			saveAccount(tempToAccount, currentUserId, tempToAccount.getId());

			activeTransaction.setFromAcc(dummyAccount);
		}
		else if(type == Transaction.TYPE_EXPENDITURE) {
			tempFromAccount = (Account) spinnerFirstAccount.getSelectedItem();

			if(tempFromAccount.getType() == Account.TYPE_CASH_ACCOUNT) {
				tempFromAccount = (CashAccount) spinnerFirstAccount.getSelectedItem();
				activeTransaction.setFromAcc(tempFromAccount);
			}
			else {
				tempFromAccount = (AccountWithBalance) spinnerFirstAccount.getSelectedItem();
				activeTransaction.setFromAcc(tempFromAccount);
			}

			if(initialFromAccount != null
					&& !initialFromAccount.equals(dummyAccount)
					&& !tempFromAccount.equals(initialFromAccount)) {
				initialFromAccount.processTransaction(Account.GAIN, initialAmount);
				saveAccount(initialFromAccount, currentUserId, initialFromAccount.getId());
			}

			if(!tempFromAccount.equals(initialFromAccount)) {
				tempFromAccount.processTransaction(Account.LOSS, newAmount);
			}
			else {
				tempFromAccount.processTransaction(Account.LOSS, balanceChange);
			}
			saveAccount(tempFromAccount, currentUserId, tempFromAccount.getId());

			activeTransaction.setToAcc(dummyAccount);
		}
		else {
			tempFromAccount = (Account) spinnerFirstAccount.getSelectedItem();
			tempToAccount = (Account) spinnerSecondAccount.getSelectedItem();

			if(tempFromAccount.getType() == Account.TYPE_CASH_ACCOUNT) {
				tempFromAccount = (CashAccount) spinnerFirstAccount.getSelectedItem();
				activeTransaction.setFromAcc(tempFromAccount);
			}
			else {
				tempFromAccount = (AccountWithBalance) spinnerFirstAccount.getSelectedItem();
				activeTransaction.setFromAcc(tempFromAccount);
			}
			if(tempToAccount.getType() == Account.TYPE_CASH_ACCOUNT) {
				tempToAccount = (CashAccount) spinnerSecondAccount.getSelectedItem();
				activeTransaction.setToAcc(tempToAccount);
			}
			else {
				tempToAccount = (AccountWithBalance) spinnerSecondAccount.getSelectedItem();
				activeTransaction.setToAcc(tempToAccount);
			}

			if(initialToAccount != null
					&& !initialToAccount.equals(dummyAccount)
					&& !tempToAccount.equals(initialToAccount)) {
				initialToAccount.processTransaction(Account.LOSS, initialAmount);
				saveAccount(initialToAccount, currentUserId, initialToAccount.getId());
			}
			if(initialFromAccount != null
					&& !initialFromAccount.equals(dummyAccount)
					&& !tempFromAccount.equals(initialFromAccount)) {
				initialFromAccount.processTransaction(Account.GAIN, initialAmount);
				saveAccount(initialFromAccount, currentUserId, initialFromAccount.getId());
			}

			if(!tempFromAccount.equals(initialFromAccount)) {
				tempFromAccount.processTransaction(Account.LOSS, newAmount);
			}
			else {
				tempFromAccount.processTransaction(Account.LOSS, balanceChange);
			}

			if(!tempToAccount.equals(initialToAccount)) {
				tempToAccount.processTransaction(Account.GAIN, newAmount);
			}
			else {
				tempToAccount.processTransaction(Account.GAIN, balanceChange);
			}

			saveAccount(tempFromAccount, currentUserId, tempFromAccount.getId());
			saveAccount(tempToAccount, currentUserId, tempToAccount.getId());

			activeTransaction.setFromAcc(tempFromAccount);
			activeTransaction.setToAcc(tempToAccount);
		}

		if(activeTransaction.getDateTime() == null) {
			activeTransaction.setDateTime(Calendar.getInstance().getTime());
		}

		activeTransaction.setCategory((TransactionCategory) spinnerCategory.getSelectedItem());

		activeTransaction.setDescription(etDescription.getText().toString().trim());


		// if id is null the transaction is new, else save it under its existing id
		if(activeTransaction.getId() == null) {
			saveTransaction(activeTransaction, currentUserId);
		}
		else {
			saveTransaction(transaction, currentUserId, transaction.getId());
		}
	}


	/**
	 * Gets the dummy account to be used as the other, unspecified side in a transaction
	 * (income - fromAcc, expenditure - toAcc)
	 */
	private void getDummyAccount() {
		dbDummyAccounts.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				for(DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
					dummyAccount = childSnapshot.getValue(CashAccount.class);
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {}
		});
	}

	/**
	 * Populate the spinners with required data from the database, and sets their current selections
	 * if necessary.
	 */
    private void setupSpinners() {
		dbAccounts.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				accountList = readAccounts(dataSnapshot);
				populateAccountSpinners();
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {}
		});

		dbTransactionCategories.child(currentUserId).addListenerForSingleValueEvent(
				new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				for(DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
					transactionCategoryList.add(childSnapshot.getValue(TransactionCategory.class));
				}
				populateTransactionCategorySpinner();
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {}
		});

		populateTypeSpinner();
	}

	public void populateAccountSpinners() {
		accountAdapter = new ArrayAdapter<>(
				TransactionEditActivity.this,
				android.R.layout.simple_spinner_item,
				accountList);

		accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerFirstAccount.setAdapter(accountAdapter);
		spinnerSecondAccount.setAdapter(accountAdapter);

		if (requestCode == TransactionOverviewActivity.TRANSACTION_EDIT_REQUEST_CODE) {
			if(transaction.getType() == Transaction.TYPE_INCOME) {
				spinnerFirstAccount.setSelection(
						getAccountPositionInAdapter(accountAdapter, transaction.getToAcc()));
			}
			else if(transaction.getType() == Transaction.TYPE_EXPENDITURE) {
				spinnerFirstAccount.setSelection(
						getAccountPositionInAdapter(accountAdapter, transaction.getFromAcc()));
			}
			else {
				spinnerFirstAccount.setSelection(
						getAccountPositionInAdapter(accountAdapter, transaction.getFromAcc()));
				spinnerSecondAccount.setSelection(
						getAccountPositionInAdapter(accountAdapter, transaction.getToAcc()));
			}
		}
	}

	public void populateTransactionCategorySpinner() {
		categoryAdapter = new ArrayAdapter<>(
				TransactionEditActivity.this,
				android.R.layout.simple_spinner_item,
				transactionCategoryList);

		categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerCategory.setAdapter(categoryAdapter);

		if (requestCode == TransactionOverviewActivity.TRANSACTION_EDIT_REQUEST_CODE) {
			spinnerCategory.setSelection(getCategoryPositionInAdapter(categoryAdapter, transaction.getCategory()));
		}
	}

	public void populateTypeSpinner() {
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
				android.R.layout.simple_spinner_dropdown_item,
				getResources().getStringArray(R.array.type_labels));
		spinnerType.setAdapter(adapter);

		if (requestCode == TransactionOverviewActivity.TRANSACTION_EDIT_REQUEST_CODE) {
			spinnerType.setSelection(transaction.getType());
		}
	}

	public int getAccountPositionInAdapter(Adapter adapter, Account account) {
    	int count = adapter.getCount();
		for(int i = 0; i < count; i++) {
			Account adapterObject = (Account) adapter.getItem(i);
			if(adapterObject.equals(account)) return i;
		}

		return -1;
	}

	public int getCategoryPositionInAdapter(Adapter adapter, TransactionCategory category) {
		int count = adapter.getCount();
		for(int i = 0; i < count; i++) {
			TransactionCategory adapterObject = (TransactionCategory) adapter.getItem(i);
			if(adapterObject.equals(category)) return i;
		}

		return -1;
	}


	public void endActivity() {
		Intent intent = new Intent();
		this.setResult(RESULT_OK, intent);
		finish();
	}
}

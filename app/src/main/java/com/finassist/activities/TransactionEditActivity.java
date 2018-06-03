package com.finassist.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.finassist.R;
import com.finassist.data.Account;
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

import static com.finassist.helpers.FirebaseDatabaseHelper.dbAccounts;
import static com.finassist.helpers.FirebaseDatabaseHelper.dbDummyAccounts;
import static com.finassist.helpers.FirebaseDatabaseHelper.dbTransactionCategories;
import static com.finassist.helpers.FirebaseDatabaseHelper.saveTransaction;

public class TransactionEditActivity extends Activity {
	private String currentUserId;
    private int requestCode;
    private Transaction transaction;
    private Account dummyAccount;

    private ArrayAdapter<Account> accountAdapter;
	private ArrayAdapter<TransactionCategory> categoryAdapter;

    private List<Account> accountList;
    private List<TransactionCategory> transactionCategoryList;

	@BindView(R.id.tvFirstAccount) TextView tvFirstAccount;
	@BindView(R.id.spinnerFirstAccount) Spinner spinnerFirstAccount;
	@BindView(R.id.tvSecondAccount) TextView tvSecondAccount;
	@BindView(R.id.llSecondAccount) LinearLayout llSecondAccount;
	@BindView(R.id.spinnerSecondAccount) Spinner spinnerSecondAccount;
	@BindView(R.id.spinnerCategory) Spinner spinnerCategory;
	@BindView(R.id.spinnerType) Spinner spinnerType;
	@BindView(R.id.etAmount) EditText etAmount;
	@BindView(R.id.etDescription) EditText etDescription;

	@BindView(R.id.btnAccept) Button btnAccept;
	@BindView(R.id.btnCancel) Button btnCancel;

    @SuppressLint("ch.twint.walletapp.lint.debouncedOnClickListener")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_edit);

        accountList = new ArrayList<>();
        transactionCategoryList = new ArrayList<>();

		ButterKnife.bind(this);

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

		etAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus) {
					double amount = Double.parseDouble(etAmount.getText().toString());
					etAmount.setText(new DecimalFormat("#.00").format(amount));
				}
			}
		});

		btn

		// Edits the current transaction or saves a new one, as required, then returns to the
		// overview activity.
		btnAccept.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(requestCode == TransactionOverviewActivity.TRANSACTION_EDIT_REQUEST_CODE) { // edit existing
					queueSaveTransaction(transaction);
				}
				else { // add new
					// TODO validation!
					queueSaveTransaction(new Transaction());
				}

				endActivity();
			}
		});

		// Returns to the main activity without changes.
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				endActivity();
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
			etAmount.setText(new DecimalFormat("#.00").format(transaction.getAmount()));
			etDescription.setText(transaction.getDescription());
        }

        setupSpinners();
    }

	/**
	 * Set the data for the new or edited transaction to be added to the database.
	 * @param activeTransaction the new or edited transaction.
	 */
	public void queueSaveTransaction(Transaction activeTransaction) {
		int type = spinnerType.getSelectedItemPosition();
		activeTransaction.setType(type);

		if(type == Transaction.TYPE_INCOME) {
			activeTransaction.setToAcc((Account) spinnerFirstAccount.getSelectedItem());
			activeTransaction.setFromAcc(dummyAccount);
		}
		else if(type == Transaction.TYPE_EXPENDITURE) {
			activeTransaction.setFromAcc((Account) spinnerFirstAccount.getSelectedItem());
			activeTransaction.setToAcc(dummyAccount);
		}
		else {
			activeTransaction.setFromAcc((Account) spinnerFirstAccount.getSelectedItem());
			activeTransaction.setToAcc((Account) spinnerSecondAccount.getSelectedItem());
		}

		if(activeTransaction.getDateTime() == null) {
			activeTransaction.setDateTime(Calendar.getInstance().getTime());
		}

		activeTransaction.setCategory((TransactionCategory) spinnerCategory.getSelectedItem());

		activeTransaction.setAmount(Double.parseDouble(etAmount.getText().toString().trim()));
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
					dummyAccount = childSnapshot.getValue(Account.class);
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
				for(DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
					accountList.add(childSnapshot.getValue(Account.class));
				}
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

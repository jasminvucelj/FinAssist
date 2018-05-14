package com.finassist.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.finassist.R;
import com.finassist.data.Account;
import com.finassist.data.Transaction;
import com.finassist.data.TransactionCategory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.finassist.activities.MainActivity.dbAccounts;
import static com.finassist.activities.MainActivity.dbDummyAccounts;
import static com.finassist.activities.MainActivity.dbTransactionCategories;
import static com.finassist.helpers.FirebaseDatabaseHelper.saveTransaction;

public class EditTransactionActivity extends Activity {
	private String currentUserId;
    private int requestCode;
    private Transaction transaction;
    private Account dummyAccount;

    private List<Account> accountList;
    private List<TransactionCategory> transactionCategoryList;

    Spinner spinnerAccount;
    Spinner spinnerCategory;
    Spinner spinnerType;
    EditText etAmount;
    EditText etDescription;

    Button btnAccept;
    Button btnCancel;

    @SuppressLint("ch.twint.walletapp.lint.debouncedOnClickListener")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_transaction);

        accountList = new ArrayList<>();
        transactionCategoryList = new ArrayList<>();

		spinnerAccount = findViewById(R.id.spinnerAccount);
		spinnerCategory = findViewById(R.id.spinnerCategory);
		spinnerType = findViewById(R.id.spinnerType);
		etAmount = findViewById(R.id.etAmount);
		etDescription = findViewById(R.id.etDescription);
		btnAccept = findViewById(R.id.btnAccept);
		btnCancel = findViewById(R.id.btnCancel);

		// TODO transfer

		btnAccept.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(requestCode == MainActivity.TRANSACTION_EDIT_REQUEST_CODE) {
					int type = spinnerType.getSelectedItemPosition();
					transaction.setType(type);

					if(type == Transaction.TYPE_INCOME) {
						transaction.setToAcc((Account)spinnerAccount.getSelectedItem());
						transaction.setFromAcc(dummyAccount);
					}
					else if(type == Transaction.TYPE_EXPENDITURE) {
						transaction.setFromAcc((Account)spinnerAccount.getSelectedItem());
						transaction.setToAcc(dummyAccount);
					}

					// TODO transfer

					transaction.setCategory((TransactionCategory) spinnerCategory.getSelectedItem());

					transaction.setAmount(Double.parseDouble(etAmount.getText().toString().trim()));
					transaction.setDescription(etDescription.getText().toString().trim());

					saveTransaction(transaction, currentUserId, transaction.getId());
				}

				endActivity();
			}
		});

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
                MainActivity.TRANSACTION_CREATE_REQUEST_CODE);

        // if editing transaction, get it and populate views with data from that transaction
        if (requestCode == MainActivity.TRANSACTION_EDIT_REQUEST_CODE) {
            transaction = (Transaction) intent.getSerializableExtra("transaction");
            populateViews();
        }

    }


	private void populateViews() {
    	try {
			etAmount.setText(new DecimalFormat("#.00").format(transaction.getAmount()));
			etDescription.setText(transaction.getDescription());
		} catch (Exception e) {
    		e.printStackTrace();
		}
		setupSpinners();
	}


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


    private void setupSpinners() {
		dbAccounts.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				for(DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
					accountList.add(childSnapshot.getValue(Account.class));
				}
				populateAccountSpinner();
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


	public void populateAccountSpinner() {
		ArrayAdapter<Account> adapter = new ArrayAdapter<Account>(
				EditTransactionActivity.this,
				android.R.layout.simple_spinner_item,
				accountList);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerAccount.setAdapter(adapter);
		// spinnerAccount.setSelection(adapter.getPosition(transaction.getOtherOrFromAccount()));

		if(transaction.getType() == Transaction.TYPE_INCOME) {
			spinnerAccount.setSelection(getAccountPositionInAdapter(adapter, transaction.getToAcc()));
		}
		else if(transaction.getType() == Transaction.TYPE_EXPENDITURE) {
			spinnerAccount.setSelection(getAccountPositionInAdapter(adapter, transaction.getFromAcc()));
		}

		// TODO transfer

	}


	public void populateTransactionCategorySpinner() {
		ArrayAdapter<TransactionCategory> adapter = new ArrayAdapter<TransactionCategory>(
				EditTransactionActivity.this,
				android.R.layout.simple_spinner_item,
				transactionCategoryList);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerCategory.setAdapter(adapter);
		// spinnerCategory.setSelection(adapter.getPosition(transaction.getCategory()));
		spinnerCategory.setSelection(getCategoryPositionInAdapter(adapter, transaction.getCategory()));
	}


	public void populateTypeSpinner() {
    	ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
				android.R.layout.simple_spinner_dropdown_item,
				getResources().getStringArray(R.array.options_labels));
		spinnerType.setAdapter(adapter);
		spinnerType.setSelection(transaction.getType());
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

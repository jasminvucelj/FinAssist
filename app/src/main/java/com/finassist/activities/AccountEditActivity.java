package com.finassist.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.finassist.R;
import com.finassist.data.Account;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

import static com.finassist.helpers.FirebaseDatabaseHelper.saveAccount;

public class AccountEditActivity extends Activity {
	private String currentUserId;
	private int requestCode;
	private Account account;

	@BindView(R.id.toolbar)
	Toolbar toolbar;
	@BindView(R.id.etAccountName)
	EditText etAccountName;
	@BindView(R.id.etAccountBalance)
	EditText etAccountBalance;
	@BindView(R.id.etAccountDescription)
	EditText etAccountDescription;

	@BindView(R.id.btnAccept)
	Button btnAccept;
	@BindView(R.id.btnCancel)
	Button btnCancel;

	@OnFocusChange(R.id.etAccountBalance)
	public void etAmount_onFocusChange(boolean hasFocus) {
		if(!hasFocus) {
			double amount = Double.parseDouble(etAccountBalance.getText().toString());
			etAccountBalance.setText(new DecimalFormat("#.00").format(amount));
		}
	}

	// btnAccept - edits the current account or saves a new one, as required, then returns to
	// the account overview activity.
	@OnClick(R.id.btnAccept)
	public void btnAccept_onClick() {
		if(requestCode == AccountOverviewActivity.ACCOUNT_EDIT_REQUEST_CODE) { // edit existing
			queueSaveAccount(account);
		}
		else { // add new
			// TODO validation!
			queueSaveAccount(new Account());
		}

		endActivity();
	}

	// btnCancel - returns to the account overview activity without changes.
	@OnClick(R.id.btnCancel)
	public void btnCancel_onClick() {
		endActivity();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_edit);

		ButterKnife.bind(this);

		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		Intent intent = getIntent();

		currentUserId = intent.getStringExtra("user_id");

		// get rq code - to find out if we're editing an account or creating a new one
		requestCode = intent.getIntExtra("request_code",
				TransactionOverviewActivity.TRANSACTION_CREATE_REQUEST_CODE);

		// if editing account, get it and display data from that account
		if (requestCode == TransactionOverviewActivity.TRANSACTION_EDIT_REQUEST_CODE) {
			account = (Account) intent.getSerializableExtra("account");
			etAccountName.setText(account.getName());
			etAccountBalance.setText(new DecimalFormat("#.00").format(account.getBalance()));
			etAccountDescription.setText(account.getDescription());
		}
	}

	/**
	 * Set the data for the new or edited account to be added to the database.
	 * @param activeAccount the new or edited account.
	 */
	public void queueSaveAccount(Account activeAccount) {
		activeAccount.setName(etAccountName.getText().toString().trim());
		activeAccount.setDescription(etAccountDescription.getText().toString().trim());
		activeAccount.setBalance(Double.parseDouble(etAccountBalance.getText().toString().trim()));
		// TODO do not support editing balance of existing account!

		// if id is null the account is new, else save it under its existing id
		if(activeAccount.getId() == null) {
			saveAccount(activeAccount, currentUserId);
		}
		else {
			saveAccount(account, currentUserId, account.getId());
		}
	}

	public void endActivity() {
		Intent intent = new Intent();
		this.setResult(RESULT_OK, intent);
		finish();
	}
}

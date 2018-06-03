package com.finassist.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.finassist.R;
import com.finassist.helpers.FirebaseDatabaseHelper;
import com.finassist.mock.Mocker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends Activity {

	private String currentUserId;

	@BindView(R.id.btnTransactionOverview)
	Button btnTransactionOverview;

	@BindView(R.id.btnStatistics)
	Button btnStatistics;

	@BindView(R.id.btnTransactionAccounts)
	Button btnTransactionAccounts;

	@BindView(R.id.btnSettings)
	Button btnSettings;

	@SuppressLint("ch.twint.walletapp.lint.debouncedOnClickListener")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		ButterKnife.bind(this);

		FirebaseAuth mAuth = FirebaseAuth.getInstance();

		FirebaseUser currentUser = mAuth.getCurrentUser();
		try {
			currentUserId = FirebaseDatabaseHelper.getCustomUserId(currentUser);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		// Mocker.generateMockUserData(currentUserId); // TEST
	}

	@OnClick(R.id.btnTransactionOverview)
	public void btnTransactionOverview_onClick() {
		Intent intent = new Intent(HomeActivity.this, TransactionOverviewActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.btnTransactionAccounts)
	public void btnTransactionAccounts_onClick() {
		Intent intent = new Intent(HomeActivity.this, AccountOverviewActivity.class);
		startActivity(intent);
	}
}

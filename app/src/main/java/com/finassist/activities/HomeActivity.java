package com.finassist.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.finassist.R;
import com.finassist.data.Account;
import com.finassist.data.AccountWithBalance;
import com.finassist.data.CashAccount;
import com.finassist.data.Transaction;
import com.finassist.data.TransactionCategory;
import com.finassist.helpers.FirebaseDatabaseHelper;
import com.finassist.mock.Mocker;
import com.finassist.views.TransactionView;
import com.github.mikephil.charting.charts.LineChart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.finassist.helpers.FirebaseDatabaseHelper.addTransactionCategories;
import static com.finassist.helpers.FirebaseDatabaseHelper.dbDummyAccounts;
import static com.finassist.helpers.FirebaseDatabaseHelper.dbTransactions;
import static com.finassist.helpers.FirebaseDatabaseHelper.initTransactionCategories;
import static com.finassist.helpers.FirebaseDatabaseHelper.readTransactions;
import static com.finassist.helpers.GraphHelper.graphSpotlightTransactions;
import static com.finassist.helpers.ObjectListHelper.filterTransactionsByCurrentMonth;
import static com.finassist.helpers.ObjectListHelper.sortTransactionsByDate;

public class HomeActivity extends Activity {

	private String currentUserId;
	private List<Transaction> transactionList;
	Transaction spotlightTransaction;

	@BindView(R.id.rlSpotlight)
	RelativeLayout rlSpotlight;

	@BindView(R.id.lastTransactionSpotlight)
	TransactionView lastTransactionSpotlight;

	@BindView(R.id.spotlightChart)
	LineChart spotlightChart;

	@BindView(R.id.btnTransactionOverview)
	Button btnTransactionOverview;

	@BindView(R.id.btnStatistics)
	Button btnStatistics;

	@BindView(R.id.btnTransactionAccounts)
	Button btnTransactionAccounts;

	@BindView(R.id.btnSettings)
	Button btnSettings;

	@OnClick(R.id.btnTransactionOverview)
	public void btnTransactionOverview_onClick() {
		Intent intent = new Intent(HomeActivity.this, TransactionOverviewActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.btnStatistics)
	public void btnStatistics_onClick() {
		Intent intent = new Intent(HomeActivity.this, StatisticsActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.btnTransactionAccounts)
	public void btnTransactionAccounts_onClick() {
		Intent intent = new Intent(HomeActivity.this, AccountOverviewActivity.class);
		startActivity(intent);
	}


	@SuppressLint("ch.twint.walletapp.lint.debouncedOnClickListener")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		ButterKnife.bind(this);

		rlSpotlight.setVisibility(View.INVISIBLE);

		FirebaseAuth mAuth = FirebaseAuth.getInstance();

		FirebaseUser currentUser = mAuth.getCurrentUser();
		try {
			currentUserId = FirebaseDatabaseHelper.getCustomUserId(currentUser);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		processUser();


	}

	/**
	 * Fetch all of the user's transactions from the Firebase database.
	 */
	public void fetchTransactions() {
		dbTransactions.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				transactionList = readTransactions(dataSnapshot);

				filterTransactionsByCurrentMonth(transactionList);
				sortTransactionsByDate(transactionList);

				if(transactionList.isEmpty()) {
					rlSpotlight.setVisibility(View.VISIBLE);
					return;
				}

				spotlightTransaction = transactionList.get(transactionList.size() - 1);

				graphSpotlightTransactions(spotlightChart, transactionList);

				lastTransactionSpotlight.setData(spotlightTransaction, currentUserId);

				rlSpotlight.setVisibility(View.VISIBLE);

			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});
	}



	public void processUser() {

		initTransactionCategories();

		dbDummyAccounts.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				if(dataSnapshot.getChildrenCount() == 0) { // simple check if new account
					FirebaseDatabaseHelper.addDummyAccount(currentUserId);
					addTransactionCategories(currentUserId);
				}

				// Mocker.generateMockUserData(currentUserId); // TEST

				fetchTransactions();
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});

	}

}

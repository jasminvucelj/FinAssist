package com.finassist.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.finassist.R;
import com.finassist.data.Transaction;
import com.finassist.helpers.FirebaseDatabaseHelper;
import com.github.mikephil.charting.charts.LineChart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.finassist.helpers.GraphHelper.graphSpotlightTransactions;
import static com.finassist.helpers.GraphHelper.statisticsGraph1;
import static com.finassist.helpers.GraphHelper.statisticsGraph2;

public class StatisticsActivity extends Activity {
	private String currentUserId;
	private List<Transaction> transactionList;

	@BindView(R.id.toolbar)
	Toolbar toolbar;

	/*
	@BindView(R.id.tvTotalBalanceValue)
	TextView tvTotalBalanceValue;

	@BindView(R.id.tvExpenditureThisMonthValue)
	TextView tvExpenditureThisMonthValue;

	@BindView(R.id.tvIncomeThisMonthValue)
	TextView tvIncomeThisMonthValue;
	*/

	@BindView(R.id.chart1)
	LineChart chart1;

	@BindView(R.id.chart2)
	LineChart chart2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics);

		ButterKnife.bind(this);

		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		FirebaseAuth mAuth = FirebaseAuth.getInstance();

		FirebaseUser currentUser = mAuth.getCurrentUser();
		try {
			currentUserId = FirebaseDatabaseHelper.getCustomUserId(currentUser);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		// TEST

		statisticsGraph1(chart1, transactionList);

		statisticsGraph2(chart2, transactionList);

	}
}

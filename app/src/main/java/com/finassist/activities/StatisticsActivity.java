package com.finassist.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.finassist.R;
import com.finassist.data.Account;
import com.finassist.data.Transaction;
import com.finassist.helpers.AccountBalanceHelper;
import com.finassist.helpers.FirebaseDatabaseHelper;
import com.finassist.views.AccountCheckBox;
import com.finassist.views.SecondaryFilterView;
import com.github.mikephil.charting.charts.LineChart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.finassist.helpers.DateHelper.getEndOfDay;
import static com.finassist.helpers.DateHelper.getFirstDayOfEarlierMonth;
import static com.finassist.helpers.DateHelper.getFirstDayOfMonth;
import static com.finassist.helpers.DateHelper.getFirstDayOfPreviousMonth;
import static com.finassist.helpers.DateHelper.getFirstDayOfWeek;
import static com.finassist.helpers.DateHelper.getFirstDayOfYear;
import static com.finassist.helpers.DateHelper.getLastDayOfEarlierMonth;
import static com.finassist.helpers.DateHelper.getStartOfDay;
import static com.finassist.helpers.DateHelper.getYesterday;
import static com.finassist.helpers.FirebaseDatabaseHelper.dbAccounts;
import static com.finassist.helpers.FirebaseDatabaseHelper.dbTransactions;
import static com.finassist.helpers.FirebaseDatabaseHelper.readAccounts;
import static com.finassist.helpers.FirebaseDatabaseHelper.readAccountsWithBalance;
import static com.finassist.helpers.FirebaseDatabaseHelper.readTransactions;
import static com.finassist.helpers.GraphHelper.graphSpotlightTransactions;
import static com.finassist.helpers.GraphHelper.statisticsChart;
import static com.finassist.helpers.GraphHelper.statisticsGraph1;
import static com.finassist.helpers.GraphHelper.statisticsGraph2;
import static com.finassist.helpers.ObjectListHelper.filterTransactionsByAnyAccount;
import static com.finassist.helpers.ObjectListHelper.filterTransactionsByCurrentMonth;
import static com.finassist.helpers.ObjectListHelper.filterTransactionsByCurrentWeek;
import static com.finassist.helpers.ObjectListHelper.filterTransactionsByCurrentYear;
import static com.finassist.helpers.ObjectListHelper.filterTransactionsByLastXMonths;
import static com.finassist.helpers.ObjectListHelper.filterTransactionsByPreviousMonth;
import static com.finassist.helpers.ObjectListHelper.filterTransactionsByStartEndDate;
import static com.finassist.helpers.ObjectListHelper.filterTransactionsByToday;
import static com.finassist.helpers.ObjectListHelper.filterTransactionsByYesterday;

public class StatisticsActivity extends Activity {
	private String currentUserId;
	private boolean accountsFetched = false, transactionsFetched = false;
	private List<Transaction> transactionList;
	private List<List<Transaction>> selectedTransactionsLists;
	private List<Account> accountWithBalanceList, selectedAccounts = new ArrayList<>();

	@BindView(R.id.toolbar)
	Toolbar toolbar;

	@BindView(R.id.progressBar)
	ProgressBar progressBar;

	@BindView(R.id.svMain)
	ScrollView svMain;

	@BindView(R.id.tvTotalBalanceValue)
	TextView tvTotalBalanceValue;

	@BindView(R.id.spinnerTime)
	Spinner spinnerTime;

	@BindView(R.id.llAccountCheckboxes)
	LinearLayout llAccountCheckBoxes;

	@BindView(R.id.chart1)
	LineChart chart1;

	@BindView(R.id.filterView1)
	SecondaryFilterView filterView1;

	/*
	@BindView(R.id.tvExpenditureThisMonthValue)
	TextView tvExpenditureThisMonthValue;

	@BindView(R.id.tvIncomeThisMonthValue)
	TextView tvIncomeThisMonthValue;

	@BindView(R.id.chart2)
	LineChart chart2;

*/
	@SuppressLint("ch.twint.walletapp.lint.debouncedOnClickListener")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics);

		ButterKnife.bind(this);

		progressBar.setVisibility(View.VISIBLE);
		svMain.setVisibility(View.GONE);

		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		filterView1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				filterView1.toggle();
				prepareDataAndDrawChart();
			}
		});

		updateTimeSpinner();

		FirebaseAuth mAuth = FirebaseAuth.getInstance();

		FirebaseUser currentUser = mAuth.getCurrentUser();
		try {
			currentUserId = FirebaseDatabaseHelper.getCustomUserId(currentUser);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		fetchAccounts();
		fetchTransactions();

		// TEST

		//statisticsGraph1(chart1, transactionList);
		//statisticsGraph2(chart2, transactionList);

	}

	private void updateTimeSpinner() {
		ArrayAdapter<String> adapterDate = new ArrayAdapter<>(this,
				android.R.layout.simple_spinner_dropdown_item,
				getResources().getStringArray(R.array.date_labels));

		spinnerTime.setAdapter(adapterDate);
	}


	public void fetchTransactions() {
		dbTransactions.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				transactionList = readTransactions(dataSnapshot);
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});

		transactionsFetched = true;
		showContent();
	}

	private void fetchAccounts() {
		dbAccounts.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				accountWithBalanceList = readAccountsWithBalance(dataSnapshot);

				updateTotalBalance();
				populateAccountCheckboxList();
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {}
		});
	}

	private void updateTotalBalance() {
		double balanceSum = 0;
		for(Account account : accountWithBalanceList) {
			balanceSum += account.getBalance();
		}
		tvTotalBalanceValue.setText(String.valueOf(balanceSum) + " kn");
	}

	private void populateAccountCheckboxList() {
		for(Account account : accountWithBalanceList) {
			AccountCheckBox accountCheckBox = new AccountCheckBox(this);
			accountCheckBox.setAccount(account);
			accountCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						selectedAccounts.add(accountCheckBox.getAccount());
					}
					else {
						selectedAccounts.remove(accountCheckBox.getAccount());
					}

					prepareDataAndDrawChart();
				}
			});
			llAccountCheckBoxes.addView(accountCheckBox);
		}

		accountsFetched = true;
		showContent();

	}


	private void showContent() {
		if(accountsFetched && transactionsFetched) {
			progressBar.setVisibility(View.GONE);
			svMain.setVisibility(View.VISIBLE);
		}
	}

	private void prepareDataAndDrawChart() {
		List<Double[]> startEndValues = new ArrayList<>();
		Date startDate = getStartDate();
		Date endDate = getEndDate();

		selectedTransactionsLists = new ArrayList<>();

		// get start/end balance values for all accounts
		for(Account account : selectedAccounts) {
			startEndValues.add(AccountBalanceHelper.getAccountBalanceStartEndValues(transactionList,
					account, startDate, endDate));

			List<Transaction> tempTransactionslist = new ArrayList<>();

			if(!(transactionList.size() > 0)){
				return;
			}
			for (Transaction t : transactionList) {
				tempTransactionslist.add(t.clone());
			}

			tempTransactionslist = filterTransactionsByAnyAccount(tempTransactionslist, account);

			tempTransactionslist = filterTransactionsByStartEndDate(tempTransactionslist,
					startDate, endDate);

			// TODO filterview, more filtering
			/*
			if(filterView.isCategoryChecked()) {
				filterByCategory(filterView.getCategorySelection());
			}

			if(filterView.isAmountChecked()) {
				filterByAmounts(filterView.getAmountMin(), filterView.getAmountMax());
			}

			if(filterView.isFromAccChecked() || filterView.isToAccChecked()) {
				filterByAccount(filterView.getFromAccSelection(), filterView.getToAccSelection());
			}

			if(filterView.isTypeChecked()) {
				filterByType(filterView.getTypeSelection());
			}
			*/

			selectedTransactionsLists.add(tempTransactionslist);

		}


		statisticsChart(chart1, selectedAccounts, startEndValues, selectedTransactionsLists);
	}

	private Date getStartDate() {
		Date date;

		switch (spinnerTime.getSelectedItemPosition()) {
			case 0: { // today
				date = getStartOfDay(Calendar.getInstance().getTime());
				break;
			}
			case 1: { // yesterday
				date = getYesterday(Calendar.getInstance().getTime());
				break;
			}
			case 2: { // current week
				date = getFirstDayOfWeek(Calendar.getInstance().getTime());
				break;
			}
			case 3: { // current month
				date = getFirstDayOfMonth(Calendar.getInstance().getTime());
				break;
			}
			case 4: { // prev. month
				date = getFirstDayOfPreviousMonth(Calendar.getInstance().getTime());
				break;
			}
			case 5: { // last 3 months
				date = getFirstDayOfEarlierMonth(Calendar.getInstance().getTime(), 3);
				break;
			}
			case 6: { // current year
				date = getFirstDayOfYear(Calendar.getInstance().getTime());
				break;
			}
			default:{
				date = new Date();
				break;
			}
		}

		return date;
	}

	private Date getEndDate() {
		Date date;

		if(spinnerTime.getSelectedItemPosition() == 4) {
			date = getLastDayOfEarlierMonth(Calendar.getInstance().getTime(), 3);
		}
		else {
			date = getEndOfDay(Calendar.getInstance().getTime());
		}

		return date;
	}
}

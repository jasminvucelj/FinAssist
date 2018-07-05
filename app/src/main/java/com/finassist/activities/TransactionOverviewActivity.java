package com.finassist.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.finassist.R;
import com.finassist.adapters.TransactionAdapter;
import com.finassist.data.Account;
import com.finassist.data.AccountWithBalance;
import com.finassist.data.CashAccount;
import com.finassist.data.Transaction;
import com.finassist.data.TransactionCategory;
import com.finassist.helpers.FirebaseDatabaseHelper;
import com.finassist.views.FilterView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.finassist.data.Transaction.removeTransactionFromList;
import static com.finassist.helpers.FirebaseDatabaseHelper.dbAccounts;
import static com.finassist.helpers.FirebaseDatabaseHelper.dbTransactions;
import static com.finassist.helpers.FirebaseDatabaseHelper.deleteTransaction;
import static com.finassist.helpers.FirebaseDatabaseHelper.readAccounts;
import static com.finassist.helpers.FirebaseDatabaseHelper.readTransactions;
import static com.finassist.helpers.FirebaseDatabaseHelper.transactionCategories;
import static com.finassist.helpers.ObjectListHelper.filterTransactionsByAmount;
import static com.finassist.helpers.ObjectListHelper.filterTransactionsByAnyAccount;
import static com.finassist.helpers.ObjectListHelper.filterTransactionsByCategory;
import static com.finassist.helpers.ObjectListHelper.filterTransactionsByCurrentMonth;
import static com.finassist.helpers.ObjectListHelper.filterTransactionsByCurrentWeek;
import static com.finassist.helpers.ObjectListHelper.filterTransactionsByCurrentYear;
import static com.finassist.helpers.ObjectListHelper.filterTransactionsByFromAccount;
import static com.finassist.helpers.ObjectListHelper.filterTransactionsByLastXMonths;
import static com.finassist.helpers.ObjectListHelper.filterTransactionsByMaxAmount;
import static com.finassist.helpers.ObjectListHelper.filterTransactionsByMinAmount;
import static com.finassist.helpers.ObjectListHelper.filterTransactionsByPreviousMonth;
import static com.finassist.helpers.ObjectListHelper.filterTransactionsByToAccount;
import static com.finassist.helpers.ObjectListHelper.filterTransactionsByToday;
import static com.finassist.helpers.ObjectListHelper.filterTransactionsByType;
import static com.finassist.helpers.ObjectListHelper.filterTransactionsByYesterday;
import static com.finassist.helpers.ObjectListHelper.sortTransactionsByDate;

public class TransactionOverviewActivity extends Activity
        implements TransactionAdapter.TransactionAdapterOnClickHandler {

    private static final String LOG_TAG = "TransactionOverviewActivity";

    public static final int TRANSACTION_CREATE_REQUEST_CODE = 10;
    public static final int TRANSACTION_EDIT_REQUEST_CODE = 11;

    @BindView(R.id.toolbar)
	Toolbar toolbar;

    @BindView(R.id.filterView)
	FilterView filterView;

    @BindView(R.id.rv_transactions)
	RecyclerView rvTransactions;

    @BindView(R.id.progress_bar)
	ProgressBar progressBar;

    @BindView(R.id.fab)
	FloatingActionButton fab;

    @BindView(R.id.tv_message)
	TextView tvMessage;

    private String currentUserId;

	private TransactionAdapter transactionAdapter;
    private List<Transaction> transactionList = new ArrayList<>();
	private List<Transaction> displayedTransactionList = new ArrayList<>();

	private List<Account> accountList = new ArrayList<>();

	@SuppressLint("ch.twint.walletapp.lint.debouncedOnClickListener")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions_overview);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

		FirebaseUser currentUser = mAuth.getCurrentUser();
		try {
			currentUserId = FirebaseDatabaseHelper.getCustomUserId(currentUser);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		ButterKnife.bind(this);

		setupViews();
		setUpSwipeToDelete();
		fetchTransactions();
		fetchAccounts();
    }

	@SuppressLint("ch.twint.walletapp.lint.debouncedOnClickListener")
	private void setupViews() {
		fab.hide();

		// Starts the TransactionEditActivity to add a new transaction.
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent (TransactionOverviewActivity.this, TransactionEditActivity.class);
				intent.putExtra("user_id", currentUserId);
				intent.putExtra("request_code", TRANSACTION_CREATE_REQUEST_CODE);
				startActivityForResult(intent, TRANSACTION_CREATE_REQUEST_CODE);
			}
		});

		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		rvTransactions.setHasFixedSize(true);
		rvTransactions.setVisibility(View.GONE);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		rvTransactions.setLayoutManager(linearLayoutManager);

		filterView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				filterView.toggle();
				filterTransactions();
			}
		});

		updateFilterViewDateSpinner();
		updateFilterViewCategorySpinners();
		updateFilterViewTypeSpinner();
	}

	/**
	 * Populates the date spinner in the FilterView.
	 */
	private void updateFilterViewDateSpinner() {
		ArrayAdapter<String> adapterDate = new ArrayAdapter<>(this,
				android.R.layout.simple_spinner_dropdown_item,
				getResources().getStringArray(R.array.date_labels));

		Spinner spinnerDate = filterView.findViewById(R.id.spinnerDate);
		spinnerDate.setAdapter(adapterDate);
	}

	/**
	 * Populates the "to" and "from" account spinners in the FilterView.
	 */
	private void updateFilterViewCategorySpinners() {
		ArrayAdapter<TransactionCategory> categoryAdapter = new ArrayAdapter<>(
				this,
				android.R.layout.simple_spinner_item,
				transactionCategories);

		categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		Spinner spinnerCategory = filterView.findViewById(R.id.spinnerCategory);
		spinnerCategory.setAdapter(categoryAdapter);
	}

	/**
	 * Populates the type spinner in the FilterView.
	 */
	private void updateFilterViewTypeSpinner() {
		ArrayAdapter<String> adapterType = new ArrayAdapter<>(this,
				android.R.layout.simple_spinner_dropdown_item,
				getResources().getStringArray(R.array.type_labels));

		Spinner spinnerType = filterView.findViewById(R.id.spinnerType);
		spinnerType.setAdapter(adapterType);
	}

	/**
	 * Populates the "to" and "from" account spinners in the FilterView.
	 */
	private void updateFilterViewAccountSpinners() {
		ArrayAdapter<Account> accountAdapter = new ArrayAdapter<>(
				this,
				android.R.layout.simple_spinner_item,
				accountList);

		accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		Spinner spinnerFromAcc = filterView.findViewById(R.id.spinnerFromAcc);
		spinnerFromAcc.setAdapter(accountAdapter);
		Spinner spinnerToAcc = filterView.findViewById(R.id.spinnerToAcc);
		spinnerToAcc.setAdapter(accountAdapter);
	}

	/**
	 * Sets up the "swipe item to delete" functionality for the RecyclerView
	 */
	private void setUpSwipeToDelete() {
		ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(
				0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
			@Override
			public boolean onMove(RecyclerView recyclerView,
								  RecyclerView.ViewHolder viewHolder,
								  RecyclerView.ViewHolder target) {
				return false;
			}

			@Override
			public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
				final int position = viewHolder.getAdapterPosition(); //get position of swiped item

				AlertDialog.Builder builder = new AlertDialog.Builder(
						TransactionOverviewActivity.this);
				builder.setMessage(R.string.dialog_delete_transaction_message);

				builder.setPositiveButton(R.string.dialog_delete,
						new DialogInterface.OnClickListener() { // when click on DELETE
					@Override
					public void onClick(DialogInterface dialog, int which) {
						transactionAdapter.notifyItemRemoved(position);
						Transaction deletedTransaction = displayedTransactionList.get(position);

						deleteTransaction(deletedTransaction, currentUserId);

						displayedTransactionList.remove(position);  //then remove item

						removeTransactionFromList(deletedTransaction, transactionList);
					}
				}).setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						transactionAdapter.notifyItemRemoved(position + 1);
						// notifies the adapter that positions of elements in the RV has changed
						transactionAdapter.notifyItemRangeChanged(position, transactionAdapter.getItemCount());
					}
				}).show();  // show alert dialog

			}
		};
		ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
		itemTouchHelper.attachToRecyclerView(rvTransactions); //set swipe to RV
	}

	/**
	 * Starts the TransactionEditActivity to edit the clicked transaction.
	 * @param transaction the selected transaction.
	 */
	@Override
    public void onClick(Transaction transaction) {
        Intent intent = new Intent (this, TransactionEditActivity.class);
        intent.putExtra("transaction", transaction);
        intent.putExtra("user_id", currentUserId);
        intent.putExtra("request_code", TRANSACTION_EDIT_REQUEST_CODE);
        startActivityForResult(intent, TRANSACTION_EDIT_REQUEST_CODE);
    }


	/**
	 * Fetch all of the user's transactions from the Firebase database.
	 */
	public void fetchTransactions() {
		dbTransactions.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				transactionList = readTransactions(dataSnapshot);

				clearSearchParams();
				filterTransactions();
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});
	}

	/**
	 * Fetch all of the user's accounts from the Firebase database.
	 */
	public void fetchAccounts() {
		dbAccounts.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				accountList = readAccounts(dataSnapshot);

				updateFilterViewAccountSpinners();
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});
	}


	/**
     * Binds the transaction data to an adapter, and then to the RecyclerView, and shows the RecyclerView,
     * hiding the progress indicator.
     */
    private void updateRecyclerView() {
    	sortTransactionsByDate(displayedTransactionList, false);

		fab.show();
		rvTransactions.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
		tvMessage.setText(R.string.message_add_transaction);

		if (displayedTransactionList.isEmpty() && filterView.isAnythingChecked()) {
			tvMessage.setText(R.string.message_transaction_selection_empty);
			progressBar.setVisibility(View.GONE);
		}

		else {
        	transactionAdapter = new TransactionAdapter(displayedTransactionList,
                    TransactionOverviewActivity.this);
            rvTransactions.setAdapter(transactionAdapter);
            rvTransactions.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            tvMessage.setText(R.string.message_transaction_edit_or_delete);
            filterView.setVisibility(View.VISIBLE);
		}
    }

	/**
	 * Filters the list of transactions based on the data in the FilterView
	 */
	private void filterTransactions() {
		displayedTransactionList = new ArrayList<>();

		if(!(transactionList.size() > 0)){
			return;
		}
		for (Transaction t : transactionList) {
			displayedTransactionList.add(t.clone());
		}

		if(filterView.isDateChecked()) {
			filterByDates(filterView.getDateSelection());
		}

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

		updateRecyclerView();
	}

	public void filterByDates(int selection) {
		switch (selection) {
			case 0: { // today
				displayedTransactionList = filterTransactionsByToday(displayedTransactionList);
				break;
			}
			case 1: { // yesterday
				displayedTransactionList = filterTransactionsByYesterday(displayedTransactionList);
				break;
			}
			case 2: { // current week
				displayedTransactionList = filterTransactionsByCurrentWeek(displayedTransactionList);
				break;
			}
			case 3: { // current month
				displayedTransactionList = filterTransactionsByCurrentMonth(displayedTransactionList);
				break;
			}
			case 4: { // prev. month
				displayedTransactionList = filterTransactionsByPreviousMonth(displayedTransactionList);
				break;
			}
			case 5: { // last 3 months
				displayedTransactionList = filterTransactionsByLastXMonths(displayedTransactionList, 3);
				break;
			}
			case 6: { // current year
				displayedTransactionList = filterTransactionsByCurrentYear(displayedTransactionList);
				break;
			}
			default:{
				break;
			}
		}
	}

	public void filterByCategory(TransactionCategory category) {
		displayedTransactionList = filterTransactionsByCategory(displayedTransactionList, category);
	}

	public void filterByAmounts(double amountMin, double amountMax) {
		if(amountMin != Double.MIN_VALUE && amountMax != Double.MAX_VALUE) {
			filterTransactionsByAmount(displayedTransactionList, amountMin, amountMax);
			return;
		}
		if(amountMin == Double.MIN_VALUE) {
			displayedTransactionList = filterTransactionsByMaxAmount(displayedTransactionList, amountMax);
			return;
		}

		displayedTransactionList = filterTransactionsByMinAmount(displayedTransactionList, amountMin);
	}

	public void filterByAccount(Account fromAcc, Account toAcc) {
		if(filterView.isFromAccChecked() && filterView.isToAccChecked()) {
			if(fromAcc.equals(toAcc)) {
				displayedTransactionList = filterTransactionsByAnyAccount(displayedTransactionList, fromAcc);
			}
			else {
				displayedTransactionList = filterTransactionsByAnyAccount(displayedTransactionList, fromAcc, toAcc);
			}
			return;
		}

		if(filterView.isFromAccChecked()) {
			displayedTransactionList = filterTransactionsByFromAccount(displayedTransactionList, fromAcc);
			return;
		}
		if(filterView.isToAccChecked()) {
			displayedTransactionList = filterTransactionsByToAccount(displayedTransactionList, toAcc);
		}
	}

	private void filterByType(int type) {
		displayedTransactionList = filterTransactionsByType(displayedTransactionList, type);
	}

	private void clearSearchParams() {
		filterView.clearSearchParams();
	}

	/**
	 * Refreshes the displayed data after returning from another activity.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		fetchTransactions();
		updateRecyclerView();
	}
}

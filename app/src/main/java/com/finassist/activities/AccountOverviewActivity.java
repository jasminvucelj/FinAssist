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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.finassist.R;
import com.finassist.adapters.AccountAdapter;
import com.finassist.data.Account;
import com.finassist.data.AccountWithBalance;
import com.finassist.data.CashAccount;
import com.finassist.helpers.FirebaseDatabaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.finassist.helpers.FirebaseDatabaseHelper.dbAccounts;
import static com.finassist.helpers.FirebaseDatabaseHelper.deleteAccount;
import static com.finassist.helpers.FirebaseDatabaseHelper.readAccounts;

public class AccountOverviewActivity extends Activity
		implements AccountAdapter.AccountAdapterOnClickHandler {

	private static final String LOG_TAG = "AccountOverviewActivity";

	public static final int ACCOUNT_CREATE_REQUEST_CODE = 10;
	public static final int ACCOUNT_EDIT_REQUEST_CODE = 11;

	@BindView(R.id.toolbar)
	Toolbar toolbar;
	@BindView(R.id.rv_accounts)
	RecyclerView rvAccounts;
	@BindView(R.id.progress_bar)
	ProgressBar progressBar;
	@BindView(R.id.fab)
	FloatingActionButton fab;
	@BindView(R.id.tv_message)
	TextView tvMessage;

	String currentUserId;

	private AccountAdapter accountAdapter;
	private List<Account> accountList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_overview);

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
		fetchAccounts();
	}

	@SuppressLint("ch.twint.walletapp.lint.debouncedOnClickListener")
	private void setupViews() {
		fab.hide();

		// Starts the TransactionEditActivity to add a new transaction.
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AccountOverviewActivity.this, AccountEditActivity.class);
				intent.putExtra("user_id", currentUserId);
				intent.putExtra("account_type", Account.TYPE_CASH_ACCOUNT);
				intent.putExtra("request_code", ACCOUNT_CREATE_REQUEST_CODE);
				startActivityForResult(intent, ACCOUNT_CREATE_REQUEST_CODE);
			}
		});

		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		rvAccounts.setHasFixedSize(true);
		rvAccounts.setVisibility(View.GONE);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		rvAccounts.setLayoutManager(linearLayoutManager);
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
						AccountOverviewActivity.this);
				builder.setMessage(R.string.dialog_delete_account_message);

				builder.setPositiveButton(R.string.dialog_delete,
						new DialogInterface.OnClickListener() { // when click on DELETE
							@Override
							public void onClick(DialogInterface dialog, int which) {
								accountAdapter.notifyItemRemoved(position);
								deleteAccount(accountList.get(position), currentUserId);
								accountList.remove(position);  //then remove item
							}
						}).setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						accountAdapter.notifyItemRemoved(position + 1);
						// notifies the adapter that positions of elements in the RV has changed
						accountAdapter.notifyItemRangeChanged(position, accountAdapter.getItemCount());
					}
				}).show();  // show alert dialog

			}
		};
		ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
		itemTouchHelper.attachToRecyclerView(rvAccounts); //set swipe to RV
	}

	/**
	 * Starts the AccountEditActivity to edit the clicked account.
	 * @param account the selected transaction.
	 */
	@Override
	public void onClick(Account account) {
		Intent intent = new Intent (this, AccountEditActivity.class);
		intent.putExtra("account", account);
		intent.putExtra("user_id", currentUserId);
		intent.putExtra("account_type", account.getType());
		intent.putExtra("request_code", ACCOUNT_EDIT_REQUEST_CODE);
		startActivityForResult(intent, ACCOUNT_EDIT_REQUEST_CODE);
	}

	/**
	 * Fetch all of the user's accounts from the Firebase database.
	 */
	public void fetchAccounts() {
		dbAccounts.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				accountList = readAccounts(dataSnapshot);

				updateRecyclerView();
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {}
		});
	}

	/**
	 * Binds the transaction data to an adapter, and then to the RecyclerView, and shows the RecyclerView,
	 * hiding the progress indicator.
	 */
	private void updateRecyclerView() {
		fab.show();

		if (!accountList.isEmpty()) {
			accountAdapter = new AccountAdapter(accountList,
					AccountOverviewActivity.this);
			rvAccounts.setAdapter(accountAdapter);
			rvAccounts.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			tvMessage.setText(R.string.message_account_edit_or_delete);
			return;
		}

		tvMessage.setText(R.string.message_add_account);
	}

	/**
	 * Refreshes the displayed data after returning from another activity.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		fetchAccounts();
		updateRecyclerView();
	}
}

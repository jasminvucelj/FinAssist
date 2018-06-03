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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.finassist.R;
import com.finassist.adapters.TransactionAdapter;
import com.finassist.data.Transaction;
import com.finassist.helpers.FirebaseDatabaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.finassist.helpers.FirebaseDatabaseHelper.dbTransactions;
import static com.finassist.helpers.FirebaseDatabaseHelper.deleteTransaction;

public class TransactionOverviewActivity extends Activity
        implements TransactionAdapter.TransactionAdapterOnClickHandler {

    private static final String LOG_TAG = "TransactionOverviewActivity";

    public static final int TRANSACTION_CREATE_REQUEST_CODE = 10;
    public static final int TRANSACTION_EDIT_REQUEST_CODE = 11;

    @BindView(R.id.rv_transactions) RecyclerView rvTransactions;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.tv_message) TextView tvMessage;

    private String currentUserId;

	private TransactionAdapter transactionAdapter;
    private List<Transaction> transactionList = new ArrayList<>();


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

		rvTransactions.setHasFixedSize(true);
		rvTransactions.setVisibility(View.GONE);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		rvTransactions.setLayoutManager(linearLayoutManager);
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
						deleteTransaction(transactionList.get(position), currentUserId);
						transactionList.remove(position);  //then remove item
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
				transactionList = new ArrayList<>();
				for(DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
					transactionList.add(childSnapshot.getValue(Transaction.class));
				}

				updateRecyclerView();
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
		fab.show();

        if (!transactionList.isEmpty()) {
        	transactionAdapter = new TransactionAdapter(transactionList,
                    TransactionOverviewActivity.this);
            rvTransactions.setAdapter(transactionAdapter);
            rvTransactions.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            tvMessage.setText(R.string.message_transaction_edit_or_delete);
            return;
        }

        tvMessage.setText(R.string.message_add_transaction);
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

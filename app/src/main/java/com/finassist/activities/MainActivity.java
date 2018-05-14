package com.finassist.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class MainActivity extends Activity
        implements TransactionAdapter.TransactionAdapterOnClickHandler {

    private static final String LOG_TAG = "MainActivity";

    public static final int TRANSACTION_CREATE_REQUEST_CODE = 10;
    public static final int TRANSACTION_EDIT_REQUEST_CODE = 11;

    @BindView(R.id.rv_transactions) RecyclerView rvTransactions;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    private String currentUserId;

    private List<Transaction> transactionList = new ArrayList<>();

    public static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static final DatabaseReference dbTransactions = database.getReference("transactions");
	public static final DatabaseReference dbAccounts = database.getReference("accounts");
	public static final DatabaseReference dbDummyAccounts = database.getReference("dummy_accounts");
	public static final DatabaseReference dbTransactionCategories = database.getReference("transaction_categories");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progress_bar);
        rvTransactions = findViewById(R.id.rv_transactions);
        rvTransactions.setHasFixedSize(true);
        rvTransactions.setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTransactions.setLayoutManager(linearLayoutManager);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        try {
            currentUserId = FirebaseDatabaseHelper.getCustomUserId(currentUser);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        // TODO move data fetching to FirebaseDatabaseHelper?

		fetchTransactions();


        // TODO recyclerView click -> editTransaction

        // TODO recyclerView swipe L/R to delete
        // TODO toolbar edit accounts, options


    }

    @Override
    public void onClick(Transaction transaction) {
        Intent intent = new Intent (this, EditTransactionActivity.class);
        intent.putExtra("transaction", transaction);
        intent.putExtra("user_id", currentUserId);
        intent.putExtra("request_code", TRANSACTION_EDIT_REQUEST_CODE);
        startActivityForResult(intent, TRANSACTION_EDIT_REQUEST_CODE);
    }


    public void fetchTransactions() {
		dbTransactions.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				transactionList = new ArrayList<>();
				for(DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
					transactionList.add(childSnapshot.getValue(Transaction.class));
				}

				setupRecyclerView();
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
    private void setupRecyclerView() {
        if (!transactionList.isEmpty()) {
            TransactionAdapter transactionAdapter = new TransactionAdapter(transactionList,
                    MainActivity.this);
            rvTransactions.setAdapter(transactionAdapter);
            rvTransactions.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		fetchTransactions();
		setupRecyclerView();
	}
}

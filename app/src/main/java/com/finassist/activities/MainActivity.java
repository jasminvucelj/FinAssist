package com.finassist.activities;

import android.app.Activity;
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

public class MainActivity extends Activity {

    private static String LOG_TAG = "MainActivity";

    private RecyclerView rvTransactions;
    private ProgressBar progressBar;

    private String currentUserId;


    private final List<Transaction> transactionList = new ArrayList<>();
    //FirebaseDatabaseHelper fdc = new FirebaseDatabaseHelper();
    //List<Transaction> transactionList = fdc.readTransactions(currentUserId);

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference dbTransactions = database.getReference("transactions");


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

        // TODO move data fetching to FirebaseDatabaseHelper!

        dbTransactions.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    transactionList.add(childSnapshot.getValue(Transaction.class));
                }

                setupRecyclerView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // TODO recyclerView click -> editTransaction
        // TODO recyclerView swipe L/R to delete
        // TODO toolbar edit accounts, options


    }


    /**
     * Binds the transaction data to an adapter, and then to the RecyclerView, and shows the RecyclerView,
     * hiding the progress indicator.
     */
    private void setupRecyclerView() {
        if (!transactionList.isEmpty()) {
            TransactionAdapter transactionAdapter = new TransactionAdapter(transactionList);
            Toast.makeText(this, "Populating RV + " +
                    String.valueOf(transactionAdapter.getItemCount()), Toast.LENGTH_LONG).show();
            rvTransactions.setAdapter(transactionAdapter);
            rvTransactions.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }


}

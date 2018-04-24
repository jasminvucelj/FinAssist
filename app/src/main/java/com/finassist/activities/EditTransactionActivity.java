package com.finassist.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.finassist.R;
import com.finassist.data.Transaction;

public class EditTransactionActivity extends Activity {
    private int requestCode;
    private Transaction transaction;

    // TODO views

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_transaction);

        Intent intent = getIntent();

        // get rq code - to find out if we're editing a transaction or creating a new one
        requestCode = intent.getIntExtra("request_code",
                MainActivity.TRANSACTION_CREATE_REQUEST_CODE);

        // if editing transaction, get it and populate views with data from that transaction
        if (requestCode == MainActivity.TRANSACTION_EDIT_REQUEST_CODE) {
            transaction = (Transaction) intent.getSerializableExtra("transaction");
            populateViews();
        }


    }

    private void populateViews() {
        // TODO
    }
}

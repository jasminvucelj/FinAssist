package com.finassist.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.finassist.R;
import com.finassist.activities.HomeActivity;
import com.finassist.activities.TransactionEditActivity;
import com.finassist.data.Transaction;

import java.text.SimpleDateFormat;

import static com.finassist.activities.TransactionOverviewActivity.TRANSACTION_EDIT_REQUEST_CODE;

public class TransactionView extends LinearLayout {
	Transaction transaction;
	String currentUserId;
	
	TextView tvAmount, tvDescription, tvDate, tvCategory, tvAccount;
	ImageView ivType;

	public TransactionView(Context context) {
		this(context, null, 0);
	}

	public TransactionView(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	@SuppressLint("ch.twint.walletapp.lint.debouncedOnClickListener")
	public TransactionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TransactionView);

		String amount = a.getString(R.styleable.TransactionView_amount);
		String description = a.getString(R.styleable.TransactionView_description);
		String date = a.getString(R.styleable.TransactionView_date);
		String category = a.getString(R.styleable.TransactionView_category);
		String account = a.getString(R.styleable.TransactionView_account);
		Drawable image = a.getDrawable(R.styleable.TransactionView_image);

		a.recycle();

		amount = amount == null ? "" : amount;
		description = description == null ? "" : description;
		date = date == null ? "" : date;
		category = category == null ? "" : category;
		account = account == null ? "" : account;

		LayoutInflater.from(context).inflate(R.layout.transaction_spotlight_item, this, true);

		tvAmount = findViewById(R.id.tv_amount);
		tvDescription = findViewById(R.id.tv_description);
		tvDate = findViewById(R.id.tv_date);
		tvCategory = findViewById(R.id.tv_category);
		tvAccount = findViewById(R.id.tv_account);
		ivType = findViewById(R.id.iv_type);

		tvAmount.setText(amount);
		tvDescription.setText(description);
		tvDate.setText(date);
		tvCategory.setText(category);
		tvAccount.setText(account);
		ivType.setImageDrawable(image);
	}

	public void setData(Transaction transaction, String currentUserId) {
		this.transaction = transaction;
		this.currentUserId = currentUserId;
		updateViews();
	}

	public Transaction getTransaction() {
		return transaction;
	}
	
	private void updateViews() {
		tvAmount.setText(String.format("%.2f", transaction.getAmount()) + " kn");

		String description = transaction.getDescription();
		if(description == null || !(description.trim().length() > 0)) {
			if(transaction.getType() == Transaction.TYPE_TRANSFER) {
				description = "Transfer (" + transaction.transferAccountsToString() + ")";
			}
			else {
				description = "(" + transaction.getCategory().toString() + ")";
			}
		}
		tvDescription.setText(description);


		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		tvDate.setText(sdf.format(transaction.getDateTime()));
		tvCategory.setText(transaction.getCategory().toString());

		if (transaction.getType() == Transaction.TYPE_INCOME) {
			tvAmount.setTextColor(
					Resources.getSystem().getColor(android.R.color.holo_green_dark));
			tvAccount.setText(transaction.getToAcc().getName());
			ivType.setImageResource(R.drawable.ic_income);
		}
		else if(transaction.getType() == Transaction.TYPE_EXPENDITURE) {
			tvAmount.setTextColor(
					Resources.getSystem().getColor(android.R.color.holo_red_dark));
			tvAccount.setText(transaction.getFromAcc().getName());
			ivType.setImageResource(R.drawable.ic_expenditure);
		}
		else {
			tvAmount.setTextColor(Resources.getSystem().getColor(android.R.color.black));
			tvAccount.setText(transaction.transferAccountsToString());
			ivType.setImageResource(R.drawable.ic_transfer);
		}
	}

}

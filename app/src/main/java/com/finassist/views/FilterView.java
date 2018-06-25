package com.finassist.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.finassist.R;
import com.finassist.data.Account;
import com.finassist.data.Transaction;
import com.finassist.data.TransactionCategory;

public class FilterView extends LinearLayout {

	LinearLayout llCollapsed, llExpanded;
	EditText etAmountMin, etAmountMax;
	Spinner spinnerDate, spinnerCategory, spinnerFromAcc, spinnerToAcc, spinnerType;
	CheckBox cbDate, cbCategory, cbAmount, cbToAcc, cbFromAcc, cbType;

	public FilterView(Context context) {
		this(context, null, 0);
	}

	public FilterView(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	@SuppressLint("ch.twint.walletapp.lint.debouncedOnClickListener")
	public FilterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		LayoutInflater.from(context).inflate(R.layout.view_filter, this, true);

		llCollapsed = findViewById(R.id.llCollapsed);
		llExpanded = findViewById(R.id.llExpanded);

		etAmountMin = findViewById(R.id.etAmountMin);
		etAmountMax = findViewById(R.id.etAmountMax);

		spinnerDate = findViewById(R.id.spinnerDate);
		spinnerCategory = findViewById(R.id.spinnerCategory);
		spinnerFromAcc = findViewById(R.id.spinnerFromAcc);
		spinnerToAcc = findViewById(R.id.spinnerToAcc);
		spinnerType = findViewById(R.id.spinnerType);

		cbDate = findViewById(R.id.cbDate);
		cbCategory = findViewById(R.id.cbCategory);
		cbAmount = findViewById(R.id.cbAmount);
		cbToAcc = findViewById(R.id.cbToAcc);
		cbFromAcc = findViewById(R.id.cbFromAcc);
		cbType = findViewById(R.id.cbType);

	}

	/**
	 * Toggle between collapsed and expanded state
	 */
	public void toggle() {
		if(llCollapsed.getVisibility() == VISIBLE) {
			llCollapsed.setVisibility(GONE);
			llExpanded.setVisibility(VISIBLE);
		}
		else {
			llCollapsed.setVisibility(VISIBLE);
			llExpanded.setVisibility(GONE);
		}
	}

	public void clearSearchParams() {
		cbDate.setChecked(false);
		cbCategory.setChecked(false);
		cbAmount.setChecked(false);
		cbToAcc.setChecked(false);
		cbFromAcc.setChecked(false);
		cbType.setChecked(false);

		etAmountMin.setText("");
		etAmountMax.setText("");

		spinnerDate.setSelection(0);
		spinnerCategory.setSelection(0);
		spinnerFromAcc.setSelection(0);
		spinnerToAcc.setSelection(0);
		spinnerType.setSelection(0);
	}

	public boolean isAnythingChecked() {
		return cbDate.isChecked()
				|| cbAmount.isChecked()
				|| cbFromAcc.isChecked()
				|| cbToAcc.isChecked()
				|| cbType.isChecked();
	}

	public boolean isDateChecked() {
		return cbDate.isChecked();
	}

	public boolean isCategoryChecked() {
		return cbCategory.isChecked();
	}

	public boolean isAmountChecked() {
		return cbAmount.isChecked();
	}

	public boolean isFromAccChecked() {
		return cbFromAcc.isChecked();
	}

	public boolean isToAccChecked() {
		return cbToAcc.isChecked();
	}

	public boolean isTypeChecked() {
		return cbType.isChecked();
	}

	public int getDateSelection() {
		return spinnerDate.getSelectedItemPosition();
	}

	public TransactionCategory getCategorySelection() {
		return (TransactionCategory) spinnerCategory.getSelectedItem();
	}

	public Account getFromAccSelection() {
		return (Account)spinnerFromAcc.getSelectedItem();
	}

	public Account getToAccSelection() {
		return (Account)spinnerToAcc.getSelectedItem();
	}

	public int getTypeSelection() {
		return spinnerType.getSelectedItemPosition();
	}

	public double getAmountMin() {
		String tempString = etAmountMin.getText().toString().trim();
		if(tempString.isEmpty()) {
			return Double.MIN_VALUE;
		}
		return Double.parseDouble(tempString);
	}

	public double getAmountMax() {
		String tempString = etAmountMax.getText().toString().trim();
		if(tempString.isEmpty()) {
			return Double.MAX_VALUE;
		}
		return Double.parseDouble(tempString);
	}
}

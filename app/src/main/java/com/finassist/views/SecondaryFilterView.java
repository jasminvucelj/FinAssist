package com.finassist.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.finassist.R;
import com.finassist.data.Account;
import com.finassist.data.TransactionCategory;

public class SecondaryFilterView extends LinearLayout {

	LinearLayout llCollapsed, llExpanded;
	EditText etAmountMin, etAmountMax;
	Spinner spinnerCategory, spinnerType;
	CheckBox cbCategory, cbAmount, cbType;

	public SecondaryFilterView(Context context) {
		this(context, null, 0);
	}

	public SecondaryFilterView(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	@SuppressLint("ch.twint.walletapp.lint.debouncedOnClickListener")
	public SecondaryFilterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		LayoutInflater.from(context).inflate(R.layout.view_filter_secondary, this, true);

		llCollapsed = findViewById(R.id.llCollapsed);
		llExpanded = findViewById(R.id.llExpanded);

		etAmountMin = findViewById(R.id.etAmountMin);
		etAmountMax = findViewById(R.id.etAmountMax);

		spinnerCategory = findViewById(R.id.spinnerCategory);
		spinnerType = findViewById(R.id.spinnerType);

		cbCategory = findViewById(R.id.cbCategory);
		cbAmount = findViewById(R.id.cbAmount);
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
		cbCategory.setChecked(false);
		cbAmount.setChecked(false);
		cbType.setChecked(false);

		etAmountMin.setText("");
		etAmountMax.setText("");

		spinnerCategory.setSelection(0);
		spinnerType.setSelection(0);
	}

	public boolean isAnythingChecked() {
		return cbAmount.isChecked()
				|| cbCategory.isChecked()
				|| cbType.isChecked();
	}

	public boolean isCategoryChecked() {
		return cbCategory.isChecked();
	}

	public boolean isAmountChecked() {
		return cbAmount.isChecked();
	}

	public boolean isTypeChecked() {
		return cbType.isChecked();
	}

	public TransactionCategory getCategorySelection() {
		return (TransactionCategory) spinnerCategory.getSelectedItem();
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
